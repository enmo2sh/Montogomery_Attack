package Asignment2;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.Random;

public class Part2 {
    public static void main(String[] args){
        int [] lengths={3,5,10,20,50,100};
        for (int i=0;i<6;i++){
            System.out.println("length = "+lengths[i]);
           // private exponent
             BigInteger d = new BigInteger(
                "1801d152befc69b1134eda145bf6c94e224fa1acee36f06826436c609840a776a532911ae48101a460699fd9424a1d51329804fa23cbec98bf95cdb0dbc900c05c5a358f48228ab03372b25610b0354d0e4a8c57efe86b1b2fb9ff6580655cdabddb31d7a8cfaf99e7866ba0d93f7ee8d1aab07fc347836c03df537569ab9fcfca8ebf5662feafbdf196bb6c925dbc878f89985096fabd6430511c0ca9c4d99b6f9f5dd9aa3ddfac12f6c2d3194ab99c897ba25bf71e53cd33c1573e242d75c48cd2537d1766bbbf4f7235c40ce3f49b18e00c874932412743dc28b7d3d32e85c922c1d9a8e5bf4c7dd6fe4545dd699295d51945d1fc507c24a709e87561b001",
                16);

             d=d.shiftRight(d.bitLength()-lengths[i]);

            int[] result =Calculate_for_each_length (d);
            int sum =0;
            for(int j=0;j<result.length;j++)
                sum+=result[j];
            System.out.println("succeeded  "+sum+"  over 20 times ");
            System.out.println("*****************************************");
        }
    }

    private static int[] Calculate_for_each_length(BigInteger d){
        int [] result =new int[20];
       for(int i=0;i<20;i++)
            result[i]=Calculate_experiment(d);

        return result;
    }

    private static  boolean modExp(BigInteger a, BigInteger exponent, BigInteger N , boolean bit) {
        boolean res = false;
        Montgomery_M_M obj =new Montgomery_M_M();
        BigInteger [] temp =obj.Calculate_unchanged_values(N);
        // Note: This code assumes the most significant bit of the exponent is 1, i.e., the exponent is not zero.
        BigInteger result = a;
       result = obj.mog_mod_mul(result,result,N,temp[0],temp[1],temp[2],2);
        if(bit)   // assume target bit equals 1
            result = obj.mog_mod_mul(result,a,N,temp[0],temp[1],temp[2],2);

        int expBitlength = exponent.bitLength();
        for (int i = expBitlength - 3; i >= 0; i--) {
            result = obj.mog_mod_mul(result,result,N,temp[0],temp[1],temp[2],2);
            if(i == expBitlength -3)
                res = obj.check();

            if (exponent.testBit(i))
                result = obj.mog_mod_mul(result,a,N,temp[0],temp[1],temp[2],2);

        }
        return res;
    }

    // it outputs 1 when it successes and 0 otherwise
    private static int Calculate_experiment(BigInteger d){
        Montgomery_M_M obj = new Montgomery_M_M();
        // assume target bit = 1
        LinkedList<BigInteger> t10 =new LinkedList<>();
        LinkedList<BigInteger> t11=new LinkedList<>();

        // assume target bit = 0
        LinkedList<BigInteger> t00 = new LinkedList<>();
        LinkedList<BigInteger> t01 = new LinkedList<>();
        for(int i=0;i<10000;i++){
            Random rnd = new Random();
            BigInteger modulus = new BigInteger(
                    "a12360b5a6d58b1a7468ce7a7158f7a2562611bd163ae754996bc6a2421aa17d3cf6d4d46a06a9d437525571a2bfe9395d440d7b09e9912a2a1f2e6cb072da2d0534cd626acf8451c0f0f1dca1ac0c18017536ea314cf3d2fa5e27a13000c4542e4cf86b407b2255f9819a763797c221c8ed7e7050bc1c9e57c35d5bb0bddcdb98f4a1b58f6d8b8d6edb292fd0f7fa82dc5fdcd78b04ca09e7bc3f4164d901b119c4f427d054e7848fdf7110352c4e612d02489da801ec9ab978d98831fa7f872fa750b092967ff6bdd223199af209383bbce36799a5ed5856f587f7d420e8d76a58b398ef1f7b290bc5b75ef59182bfa02fafb7caeb504bd9f77348aea61ae9",
                    16);
            BigInteger a = new BigInteger(modulus.bitLength() - 1, rnd);

            // assume target bit equals 1
            long startTime=System.nanoTime();
            boolean result1 = modExp(a,d,modulus,true);
            long time = System.nanoTime()-startTime ;
            if(result1)
                t11.add(BigInteger.valueOf(time));
            else
                t10.add(BigInteger.valueOf(time));
            // assume target bit equals 0
            long startTime2=System.nanoTime();
            boolean result0 = modExp(a,d,modulus,false);
            long time2 = System.nanoTime()-startTime2 ;
            if(result0)
                t01.add(BigInteger.valueOf(time2));
            else
                t00.add(BigInteger.valueOf(time2));
        }
        BigInteger avg10 = average(t10);
        BigInteger avg11 = average(t11);
        BigInteger avg01 = average(t01);
        BigInteger avg00 = average(t00);
        if((avg11.compareTo(avg10)>0)&&(!(((avg00.subtract(avg01)).abs()).compareTo((avg11.subtract(avg10)).abs())>0)))
            return 1;
        else
            return 0;

    }

    private static BigInteger average (LinkedList<BigInteger> list){
        if(list.size()==0)
            return BigInteger.valueOf(0);
        BigInteger sum =new BigInteger("0");
        for(int i=0;i<list.size();i++)
            sum = sum.add(list.get(i));
        BigInteger avg = sum.divide(BigInteger.valueOf(list.size()));
        return avg ;
    }


}
