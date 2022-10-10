package rogue.model.creature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PlayerImplTest {

    private Player pl;

    @org.junit.Before
    public void init() {
    }

    @org.junit.Test
    public void testDefaultsLife() {
        // with default configs
        pl = new PlayerFactoryImpl().create();

        final var hp   = pl.getLife().getHealthPoints();
        final var exp  = pl.getLife().getExperience();
        final var food = pl.getLife().getFood();

        pl.getLife().hurt(10);
        assertEquals(hp - 10, pl.getLife().getHealthPoints());
        assertFalse(pl.getLife().isDead());
        pl.getLife().increaseExperience(10);
        assertEquals(exp + 10, pl.getLife().getExperience());
        pl.getLife().decreaseFood(10);
        assertFalse(pl.getLife().isDead());
        assertEquals(food - 10, pl.getLife().getFood());
        pl.getLife().decreaseFood(food - 10);
        assertTrue(pl.getLife().isDead());
    }

    @org.junit.Test
    public void testExplicitLife() {
        // with explicit configs
        final var hp   = 3;
        final var str  = 50;
        final var exp  = 20;
        final var food = 10;

        final PlayerLifeImpl.Builder lifeBuilder = new PlayerLifeImpl.Builder();
        pl = new PlayerFactoryImpl().createByLife(lifeBuilder.initExperience(exp)
                .initFood(food)
                .initStrength(str)
                .initHealthPoints(hp)
                .build());

        pl.getLife().hurt(10);
        assertEquals(0, pl.getLife().getHealthPoints());
        assertTrue(pl.getLife().isDead());
        pl.getLife().increaseExperience(10);
        assertEquals(exp + 10, pl.getLife().getExperience());
        pl.getLife().decreaseFood(10);
        assertEquals(0, pl.getLife().getFood());
    }

    @org.junit.Test
    public void testMaxHealthPoints() {
        pl = new PlayerFactoryImpl().create(); // default values and configs
        assertEquals(pl.getLife().getHealthPoints(), pl.getLife().getMaxHealthPoints());
        final int delta = 355; // integer to obtain: level = 10 and maxHp = 120
        pl.getLife().increaseExperience(delta);
        final int actualLevel = 10;
        final int actualMaxHp = 120;
        assertEquals(actualLevel, pl.getLife().getLevel());
        assertEquals(actualMaxHp, pl.getLife().getMaxHealthPoints());
        pl.getLife().powerUp(actualMaxHp);
        assertEquals(pl.getLife().getMaxHealthPoints(), pl.getLife().getHealthPoints());
        pl.getLife().hurt(actualMaxHp);
        assertEquals(0, pl.getLife().getHealthPoints());
        assertTrue(pl.getLife().isDead());
    }

    @org.junit.Test
    public void testMaxFood() {
        pl = new PlayerFactoryImpl().create();
        // Exceeds max food
        pl.getLife().increaseFood(pl.getLife().getMaxFood() - pl.getLife().getFood() + 1);
        assertEquals(pl.getLife().getMaxFood(), pl.getLife().getFood());
        pl.getLife().decreaseFood(pl.getLife().getMaxFood() + 1);
        assertEquals(0, pl.getLife().getFood());
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testMultipleBuild() {
        // cannot be built multiple times...
        final PlayerLifeImpl.Builder lifeBuilder = new PlayerLifeImpl.Builder();
        lifeBuilder.build();
        lifeBuilder.build();
    }

}