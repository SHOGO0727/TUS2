import java.util.*;

public class BetterScan{
  
  int intScan(String in){
    Scanner sc = new Scanner(System.in);
    int ans = 0;

    while(true){
      System.out.print(in+"=");
      String input = sc.next();
      try{
        ans = Integer.parseInt(input);
        if(ans>0){break;}
        else{System.out.println("Type positive integer");}
      }
      catch(Exception e){
        System.out.println("Type positive integer");
      }
    }
    return ans;
  }

  long longScan(String in){
    Scanner sc = new Scanner(System.in);
    long ans = 0;

    while(true){
      System.out.print(in+"=");
      String input = sc.next();
      try{
        ans = Long.parseLong(input);
        if(ans>0){break;}
        else{System.out.println("Type positive long");}
      }
      catch(Exception e){
        System.out.println("Type positive long");
      }
    }
    return ans;
  }

}
