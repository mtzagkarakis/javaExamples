package manos.examples.enum_example;

import org.junit.Assert;
import org.junit.Test;

public class EnumWithAbstractMethod {
    private interface Player{
        void play();
    }
    enum PlayerType{
        FOOTBALL,
        BASKETBALL
    }
    public static class FootBallPlayer implements Player{
        @Override
        public void play() {

        }
    }
    public static class BasketBallPlayer implements Player{
        @Override
        public void play() {

        }
    }

    public Player createPlayer(PlayerType playerType){
        switch (playerType){
            case FOOTBALL: return new FootBallPlayer();
            case BASKETBALL: return new BasketBallPlayer();
            default: throw new IllegalArgumentException("No such Player");
        }
    }
    //or
    public enum PlayerTypeWithFunction{
        FOOTBALL {
            @Override
            public Player createPlayer() {
                return new FootBallPlayer();
            }
        },
        BASKETBALL {
            @Override
            public Player createPlayer() {
                return new BasketBallPlayer();
            }
        };
        public abstract Player createPlayer();
    }
    @Test
    public void strategyPatternWithEnum(){
        Assert.assertTrue(createPlayer(PlayerType.FOOTBALL) instanceof FootBallPlayer);
        Assert.assertTrue(createPlayer(PlayerType.BASKETBALL) instanceof BasketBallPlayer);

        Assert.assertTrue(PlayerTypeWithFunction.FOOTBALL.createPlayer() instanceof FootBallPlayer);
        Assert.assertTrue(PlayerTypeWithFunction.BASKETBALL.createPlayer() instanceof BasketBallPlayer);
    }
}
