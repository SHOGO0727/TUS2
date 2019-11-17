import java.util.*;

public class TUS2_nk{
    public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

      Pcalculator pc = new Pcalculator();
      DistRecon   dr = new DistRecon();
      BetterScan  bs = new BetterScan();

      //法の決定
      long p = 1000000000 + 7;
      System.out.println("p=" + p);

      //サーバ数決定
      int n = bs.intScan("n=k"); // n<p
      //しきい値決定
      int k = n;
      //int k = bs.intScan("k"); // n<p2
      //long s = (long)bs.intScan("secret"); // s<p

      //サーバidの決定
      long[] id = new long[n];
      for(int i=0;i<n;i++){
        id[i] = i+1;
      }

      //格納先確保
      //入力と出力の確保
      long a=0,b=0,c=0,d=0;
      //秘匿化秘密情報の確保
      long Aa=0,Bb=0,Cc=0,Dd=0;
      //Share_X[i]はサーバiが保持するXに対する乗法による分散値
      long[] ShareA = new long[n];
      long[] ShareB = new long[n];
      long[] ShareC = new long[n];
      long[] ShareD = new long[n];


      //変換用乱数（積和演算は2項なので2つ）
      long[] ShareU = new long[n];
      long[] ShareV = new long[n];

      long[] ShamirU = new long[n];
      long[] ShamirV = new long[n];

      //演算回数
      int cnt = 0;


while(true){
//******************************************************************************

///////////////////////事前処理//////////////////////////////////////////
//////////変換用乱数(ok確認済み)//////////////////
long U = pc.TUSrand(p);
long V = pc.TUSrand(p);
ShamirU = dr.shamirD(U,id,p,n,k);
ShamirV = dr.shamirD(V,id,p,n,k);
ShareU = dr.shareM(U,n,k,p);
ShareV = dr.shareM(V,n,k,p);
//////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////

System.out.println("制限：ab+c<"+p);

/////連続用の処理///////////////////////////////////////////////////////
String where = "-";
if(cnt!=0){
  while(true){
    System.out.print("dをどこに入力しますか？（a,b,c）:");
    where = sc.next();
    if(where.equals("a") || where.equals("b") || where.equals("c")){
      break;
    }
  }
}

int which = 0;

if(where.equals("a")){
  which = 1;
  System.out.println("a=d");
}
else{
  a = (long)bs.intScan("a");
}

if(where.equals("b")){
  which = 2;
  System.out.println("b=d");
}
else{
  b = (long)bs.intScan("b");
}

if(where.equals("c")){
  which = 3;
  System.out.println("c=d");
}
else{
  c = (long)bs.intScan("c");
}
////////////////////////////////////////////////////////////////////////

/////////////////share generation/////////////
      if(which!=1){
        long A = pc.TUSrand(p);
        ShareA = dr.shareM(A,n,k,p);
        Aa = pc.pmult(A,a,p);
        A = dr.distM(ShareA,n,k,p);
      }
      else{
        ShareA = ShareD.clone();
        Aa = Dd;
      }

      if(which!=2){
        long B = pc.TUSrand(p);
        ShareB = dr.shareM(B,n,k,p);
        Bb = pc.pmult(B,b,p);
      }
      else{
        ShareB = ShareD.clone();
        Bb = Dd;
      }

      if(which!=3){
        long C = pc.TUSrand(p);
        ShareC = dr.shareM(C,n,k,p);
        Cc = pc.pmult(C,c,p);
      }
      else{
        ShareC = ShareD.clone();
        Cc = Dd;
      }
///////////////////////////////////////

/////////Secure Product-Sum////////////
/////乱数比は2つ生成する。
long[] D_ABUi = new long[n];
long[] D_CVi = new long[n];


////各サーバは乱数比の断片を生成ok
for(int i=0;i<n;i++){
  ShareD[i] = pc.TUSrand(p);
  long ABU = pc.pmult(ShareA[i],ShareB[i],p);
       ABU = pc.pmult(ABU,ShareU[i],p);
  D_ABUi[i] = pc.pmult(ShareD[i],pc.pinv(ABU,p,p),p);

  long CV  = pc.pmult(ShareC[i],ShareV[i],p);
  D_CVi[i] = pc.pmult(ShareD[i],pc.pinv(CV,p,p),p);
}

////乱数比の断片を復元(公開値)ok
long D_ABU = dr.distM(D_ABUi,n,k,p);
long D_CV = dr.distM(D_CVi,n,k,p);

////////各サーバの実際の積和演算ok
long[] Ddi = new long[n];
for(int i=0;i<n;i++){
long ans1 = pc.pmult(D_ABU,ShamirU[i],p);
long ans2 = pc.pmult(D_CV,ShamirV[i],p);
ans1 = pc.pmult(ans1,pc.pmult(Aa,Bb,p),p);
ans2 = pc.pmult(ans2,Cc,p);
Ddi[i] = pc.padd(ans1,ans2,p);
}

Dd = dr.shamirR(Ddi,id,p,n,k);

System.out.println("calculation of d=a*b+c done");

/////////////reconstruction////////////

System.out.print("演算を続けますか？（y,n）:");
String fin;
while(true){
  fin = sc.next();
  if(fin.equals("y") || fin.equals("n")){
    break;
  }
  System.out.print("演算を続けますか？（y,n）:");
}
if(fin.equals("n")){
   long D = dr.distM(ShareD,n,k,p);
   long ans = pc.pdiv(Dd,D,p);
  System.out.println("output="+ans);
  break;
}
System.out.println("");
cnt++;

///////////////////////////////////////

//******************************************************************************
}

    }
}

/*
long[] share = dr.shamirD(s,id,p,n,k);
long ans = dr.shamirR(share,id,p,n,k);
System.out.println("");
System.out.println("reconstructed value = " + ans);
*/
