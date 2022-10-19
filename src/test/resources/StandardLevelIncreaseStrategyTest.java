package rogue.model.creature;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test class
 * @see https://github.com/DanySK/Student-Project-OOP20-Chiarini-Pezzi-Quarneti-Tassinari-rogue/blob/main/src/test/java/rogue/model/creature/StandardLevelIncreaseStrategyTest.java
 */
public class StandardLevelIncreaseStrategyTest {

    private static final int LEVEL_12 = 12;
    private static final int EXP_LEVEL_12 = 789;
    private static final int EXP_LEVEL_20 = 12_000;
    private static final int LEVEL_20 = 20;
    private static final int EXP_LEVEL_2 = 7;
    private static final int LEVEL_2 = 2;
    private LevelIncreaseStrategy strategy;

    @org.junit.Before
    public void init() {
        strategy = new StandardLevelIncreaseStrategy();
    }

    @Test
    public void test() {
        assertEquals(LEVEL_2, strategy.getLevel(EXP_LEVEL_2));
        assertEquals(LEVEL_20, strategy.getLevel(EXP_LEVEL_20));
        assertEquals(LEVEL_12, strategy.getLevel(EXP_LEVEL_12));
    }

}