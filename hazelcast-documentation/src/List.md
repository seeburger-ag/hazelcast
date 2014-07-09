

## List

Hazelcast List is very similar to Hazelcast Set but it allows duplicate elements.

* Besides allowing duplicate elements, Hazelcast List preserves the order of elements.
* Hazelcast List is non-partitioned data structure where values and each backup is represented by its own single partition.
* Hazelcast List cannot be scaled beyond the capacity of a single machine.
* There is no batching while iterating over List, all items will be copied to local and iteration will occur locally.

### Sample List Code

```java
import com.hazelcast.core.Hazelcast;
import java.util.List;
import java.util.Iterator;
import com.hazelcast.config.Config;

Config cfg = new Config();
HazelcastInstance hz = Hazelcast.newHazelcastInstance(cfg);

java.util.List list = hz.getList("IBM-Quote-Frequency");
list.add(new Price(10));
list.add(new Price(11));
list.add(new Price(12));
list.add(new Price(11));
list.add(new Price(12));
        
//....
Iterator it = list.iterator();
while (it.hasNext()) { 
    Price price = (Price) it.next(); 
    //analyze
}
```

### Event Registration and Configuration

Hazelcast List uses `ItemListener` to listen to events which occur when items are added and removed.


```java
import java.util.Queue;
import java.util.Map; 
import java.util.Set; 
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.ItemListener;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.EntryEvent; 
import com.hazelcast.config.Config;

public class Sample implements ItemListener{

    public static void main(String[] args) { 
        Sample sample = new Sample();
        Config cfg = new Config();
        HazelcastInstance hz = Hazelcast.newHazelcastInstance(cfg);
        IList   list   = hz.getList   ("default");
        list.addItemListener  (sample, true); 
        
        Price price = new Price(10, time1)
        list.add(price);
        list.remove(price);
    } 

    public void itemAdded(Object item) {
        System.out.println("Item added = " + item);
    }

    public void itemRemoved(Object item) {
        System.out.println("Item removed = " + item);
    }     
}
       
```


<br> </br>

<font color="red">
***Related Information***
</font>

*Please refer to [Listener Configurations](#listener-configurations).*

<br> </br>


