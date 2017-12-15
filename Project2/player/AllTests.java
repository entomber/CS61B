package player;

import DataStructures.Graph.DepthFirstPathsTest;
import DataStructures.Graph.SymbolGraphTest;
import DataStructures.List.DListTest;
import DataStructures.Stack.StackTest;
import DataStructures.dict.HashTableChainedTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    GameBoardTest.class,
    GameTreeTest.class,
    HashTableChainedTest.class,
    DepthFirstPathsTest.class,
    SymbolGraphTest.class,
    DListTest.class,
    StackTest.class})
public class AllTests {
}
