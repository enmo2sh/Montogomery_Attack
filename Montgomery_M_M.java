package Asignment2;
import java.math.BigInteger;

public class Montgomery_M_M {
     private boolean enter_to_reduction = false;

    public BigInteger mog_mod_mul(BigInteger a, BigInteger b, BigInteger N, BigInteger R, BigInteger n_bar, BigInteger R_Inverse, int part) {
        // set enter_to_reduction to false
        enter_to_reduction = false;
        // calculate a'= a R mod N        b'= b R mod N
        BigInteger a_bar = (a.multiply(R)).mod(N);
        BigInteger b_bar = (b.multiply(R)).mod(N);
        // t= a' b'    m= t N' mod R        t= (t + mN) /R
        BigInteger t = a_bar.multiply(b_bar);
        BigInteger m = (t.multiply(n_bar)).mod(R);
        t = (t.add(m.multiply(N))).divide(R);
        BigInteger c_bar = BigInteger.valueOf(0);
        // if t <N  c'=t    else    c'= t-N
        if (t.compareTo(N) < 0)
            c_bar = t;
        else {
            if (part == 1)
                c_bar = (t.subtract(N));
            else if (part == 2) {
                enter_to_reduction = true ;
                for (int i = 0; i < 10; i++)
                    c_bar = (t.subtract(N));
            }
        }
        // c = c' R^(-1) mod N
        return ((c_bar.mod(N)).multiply(R_Inverse)).mod(N);
    }

    public BigInteger[] Calculate_unchanged_values(BigInteger N) {
        // R and n_bar and R_Inverse should calculated once as they don't change
        // N should be odd so ever power of 2 is co prime of N
        // choose r to be power of 2 greater than n
        BigInteger R = BigInteger.valueOf(2).pow(N.bitLength());;
        // calculate N'= N^(-1) mod R
        BigInteger n_bar = (N.modInverse(R)).multiply(BigInteger.valueOf(-1));
        // calculate R^(-1) mod N
        BigInteger R_Inverse = R.modInverse(N);
        BigInteger[] result = new BigInteger[3];
        result[0] = R;
        result[1] = n_bar;
        result[2] = R_Inverse;
        return result;
    }

    public boolean check (){
        return enter_to_reduction ;
    }

}