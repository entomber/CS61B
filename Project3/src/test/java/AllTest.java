import dict.HashTableChainedTest;
import graph.WUGraphTest;
import list.DListTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    graphalg.KruskalTest.class,
    WUGraphTest.class,
    ListSortsTest.class,
    DListTest.class,
    HashTableChainedTest.class})
public class AllTest {
}
