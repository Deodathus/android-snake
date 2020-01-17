package com.example.andro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.andro.engine.GameEngine;
import com.example.andro.enums.Direction;
import com.example.andro.enums.GameState;
import com.example.andro.views.SnakeView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private GameEngine gameEngine;
    private SnakeView snakeView;
    private final Handler handler = new Handler();
    private final long updateDelay = 500;

    private float prevX,prevY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameEngine = new GameEngine();
        gameEngine.initGame();

        snakeView = (SnakeView)findViewById(R.id.SnakeView);
        //snakeView.setSnakeViewMap(gameEngine.getMap());
        //snakeView.invalidate();
        snakeView.setOnTouchListener(this);

        startUpdateHandler();
    }

    private void startUpdateHandler(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gameEngine.Update();

                if(gameEngine.getCurrentGameState() == GameState.Running){
                    handler.postDelayed(this, updateDelay);

                }
                if (gameEngine.getCurrentGameState() == GameState.Lost){
                    OnGameLost();
                }

                snakeView.setSnakeViewMap(gameEngine.getMap());
                snakeView.invalidate();
            }
        }, updateDelay);
    }

    private void OnGameLost(){
        Toast.makeText(this,"Game over.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                prevX = event.getX();
                prevY = event.getY();

                break;
            case MotionEvent.ACTION_UP:
                float newX = event.getX();
                float newY = event.getY();

                //onlicza zmiane
                if(Math.abs( newX- prevX) > Math.abs(newY- prevY)){
                    //lewo-prawo
                    if (newX >prevX){
                        //prawo
                        gameEngine.UpdateDirection(Direction.East);
                    }else {
                        //lewo
                        gameEngine.UpdateDirection(Direction.West);
                    }
                }else {
                    //up do
                    if (newY > prevY){
                        gameEngine.UpdateDirection(Direction.North);
                    }else{
                        gameEngine.UpdateDirection(Direction.South);
                    }
                }
                break;
        }

        return true;
    }
}
