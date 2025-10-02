package com.example.asmt2;

public class Ball {
    float x, y, radius, velocity;
    long life;
    long startTime;

    Ball(float x, float y) {
        this.x = x;
        this.y = y;
        this.radius = 10;
        this.velocity = 3;
        this.life = 5000;
        startTime = System.currentTimeMillis();
    }

    boolean isExpired() {
        // timeAlive = Current time - start time
        return System.currentTimeMillis() - startTime >= life;
    }

}
