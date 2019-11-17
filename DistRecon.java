import java.util.*;

public class DistRecon{

    long[] shamirD(long secret, long[] id, long p, int n, int k){
      Pcalculator pc = new Pcalculator();
      long[] a = new long[k];
      a[0] = secret;
      for(int i=1;i<k;i++){
        a[i]= pc.TUSrand(p);
      }
      long[] share = new long[n];
      for(int i=0;i<n;i++){
        long xi = 1;
        for(int j=0;j<k;j++){
          share[i] += pc.pmult(a[j],xi,p);
          share[i] %=p;
          xi = pc.pmult(xi,id[i],p);
        }
      }
      return share;
    }

    long shamirR(long[] share, long[] id, long p, int n, int k){
      Scanner sc = new Scanner(System.in);
      long[] useId = new long[k];
      long[] useShare = new long[k];
      int[] select = new int[k];

      int[] same = new int[n];

      for(int i=0;i<k;i++){
        select[i] = n;
      }

      for(int i=0;i<k;i++){
        while(select[i]>n-1 || select[i]<0 || same[select[i]]==1){
          System.out.print("select available server=");
          String input = sc.next();
          try{
            select[i] = Integer.parseInt(input)-1;
            if(same[select[i]]==1){
              System.out.println("The server has been selected.");
            }
          }
          catch(Exception e){
            System.out.println("Type available integer.");
          }
        }
        if(same[select[i]]==1){
          i--;
        }
        else{
          same[select[i]] = 1;
        }
      }

      for(int i=0;i<k;i++){
        useId[i] = id[select[i]];
        useShare[i] = share[select[i]];
      }

      System.out.print("selected server=(");
      for(int i=0;i<k;i++){
        if(i!=k-1){System.out.print((select[i]+1)+",");}
        else{System.out.println((select[i]+1)+")");}
      }
      return interpolate(useShare,useId,p,k);
    }

    long interpolate(long[] share,long[] id,long p,int k){ //Lagrange
      Pcalculator pc = new Pcalculator();
      //System.out.println("start interpolation");
      long ans=0;
      for(int i=0;i<k;i++){
        long seki=1;
        for(int j=0;j<k;j++){
          if(i!=j){
            long bunbo = pc.pinv(id[i]-id[j]+p,p,p);
            seki *= pc.pmult(-id[j]+p,bunbo,p);
            seki %= p;
          }
        }
        ans += pc.pmult(share[i],seki,p);
        ans %= p;
      }
      //long endR = System.currentTimeMillis();
      //long endR = System.nanoTime();
      //System.out.println("end interpolation");
      return ans;
    }

    long[] shareM(long s, int n, int k,long p){
      Pcalculator pc = new Pcalculator();
      long[] share = new long[n];
      long a = 1;
      for(int i=0;i<n-1;i++){
        share[i] = pc.TUSrand(p);
        a =pc.pmult(a,share[i],p);
      }
      share[n-1] = pc.pmult(s,pc.pinv(a,p,p),p);
      return share;
    }

    long distM(long[] share, int n, int k,long p){
      Pcalculator pc = new Pcalculator();
      long ans = 1;
      for(int i=0;i<n;i++){
        ans = pc.pmult(ans,share[i],p);
      }
      return ans;
    }


}
