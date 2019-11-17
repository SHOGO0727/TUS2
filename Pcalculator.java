import java.util.*;

public class Pcalculator{

    long padd(long a,long b,long p){
      return (a + b) % p;
    }

    long psub(long a,long b,long p){
      return (a - b + p) % p;
    }

    long pmult(long a,long b,long p){
      return (a * b) % p;
    }

    long pdiv(long a,long b,long p){
      return (a * pinv(b,p,p)) % p;
    }

    long ppow(long a,long b,long p){ //ans = a^b mod p
      long ans = 1;
      for(long i=0;i<b;i++){
        ans *= a;
        ans %= p;
      }
      return ans;
    }

    long TUSrand(long p){
      return (long)(Math.random()*(p-1)+1); //1,2,...,p-1
    }

    long pinv(long a, long b, long p){
      long r0 = a; long r1 = b;
      long x0 = 1; long x1 = 0;
      long y0 = 0; long y1 = 1;
      while(r1>0){
        long q = r0/r1;
        long r2 = r0 % r1;
        long x2 = x0 -pmult(q,x1,p)+p;
        x2 %= p;
        long y2 = y0 -pmult(q,y1,p)+p;
        y2 %= p;
        r0 = r1; r1 = r2;
        x0 = x1; x1 = x2;
        y0 = y1; y1 = y2;
      }
      return x0;
    }
    
}
