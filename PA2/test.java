import java.util.ArrayList;

public class test {
    public static void main(String [] args){
        MinHeap mH = new MinHeap();
    mH.add(1, 5);
mH.add(11, 6);
mH.add(2, 6);

mH.add(3, 4);
mH.add(10, 3);
mH.add(9, 6);
ArrayList<Integer> elements = mH.getHeap();
System.out.println("MinHeap with default degree");
for (int i=0; i<elements.size(); i++){
System.out.print(elements.get(i));
}
System.out.println();
/* produce the output
MinHeap with default degree
10 3 2 11 1 9
*/
    }
}
