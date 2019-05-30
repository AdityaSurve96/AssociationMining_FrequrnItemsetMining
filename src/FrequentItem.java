public class FrequentItem {

        public String itemSet;
        public int count;
        public int length;
        public FrequentItem()
        {
            itemSet = "";
            count = 0;
            length =0;
        }
        public FrequentItem(String str)
        {
            itemSet = str;
            count = -1;
            length =0;
        }    
    }