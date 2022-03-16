package DSCoinPackage;

import HelperClasses.CRF;

public class BlockChain_Malicious {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock[] lastBlocksList;

  public static boolean checkTransactionBlock (TransactionBlock tB) {
      CRF ob = new CRF(64);
      int flag = 0;
      int x=1;
      if(tB.previous == null)
      {
        if( tB.dgst.equals(ob.Fn(start_string+"#"+ tB.trsummary+"#" + tB.nonce)) 
            && 
            tB.dgst.substring(0,4).equals("0000")
            &&
            tB.trsummary.equals(tB.Tree.Build(tB.trarray)))
            {
              flag = 1;
            }
      }   
      else
      {
        if( tB.dgst.equals(ob.Fn(tB.previous.dgst+"#"+ tB.trsummary+"#" + tB.nonce)) 
            && 
            tB.dgst.substring(0,4).equals("0000")
            &&
            tB.trsummary.equals(tB.Tree.Build(tB.trarray)) )
            {
              flag = 1;
            }
      }
      for(int i=0;i<tB.trarray.length;i++)
      {
        if(!tB.checkTransaction(tB.trarray[i]))
        {
          x=0;
        }
      }
      if(flag==1 && x==1)
      {
        return true;
      }
      return false;
      
  }

  public TransactionBlock FindLongestValidChain () {
    TransactionBlock[] temp1 = new TransactionBlock[lastBlocksList.length];
    TransactionBlock[] temp2 = new TransactionBlock[lastBlocksList.length];
    int[] count = new int[lastBlocksList.length];
    int p=0;
    while(lastBlocksList[p]!=null)
    {
      //System.out.println(lastBlocksList[p]);
      p++; 
    }
    
    for(int i=0;i<this.lastBlocksList.length;i++)
    {
      temp1[i] = lastBlocksList[i];
      temp2[i] = lastBlocksList[i];
      count[i] = 0;
    }
    for(int i=0;i<p;i++)
    {
      while(temp1[i].previous!=null)
      {
        if(!checkTransactionBlock(temp1[i]))
        {
          
          temp2[i] = temp1[i].previous;
          count[i] = 0;
        }
        //System.out.println("f");
        count[i]++;
        temp1[i] = temp1[i].previous;
      }
      //System.out.println(checkTransactionBlock(temp1[i]) + " " + i + temp1[i]);
      if(checkTransactionBlock(temp1[i]))
      {
        count[i]++;
      }
      if(temp1[i].previous==null && !checkTransactionBlock(temp1[i]))
      {
        //System.out.println("bf");
        return null;
      }
    }
    int x = count[0];
    int index = 0;
    for(int j=1;j<p;j++)
    {
      if(Math.max(x,count[j])==count[j])
      {
        x = count[j];
        index = j;
      }
    }
    return temp2[index];
  }

  public void InsertBlock_Malicious (TransactionBlock newBlock) 
  {//first of all initialize lastblock wali array same in moderator........................;}|
    //TransactionBlock last = this.FindLongestValidChain();//sjdsidhbdhdojojao
    //BlockChain_Honest obj = new BlockChain_Honest();
    //obj.lastBlock = this.FindLongestValidChain();
    TransactionBlock last = this.FindLongestValidChain();
    //System.out.println(last);
    //obj.tr_count = this.tr_count;

    //obj.InsertBlock_Honest(newBlock);
    
    CRF ob = new CRF(64);
    newBlock.nonce = "1000000001";
    if(last==null)
    {
      newBlock.dgst = ob.Fn(start_string+ "#" + newBlock.trsummary+ "#"+ newBlock.nonce);
      while(!newBlock.dgst.substring(0,4).equals("0000"))
      {
        newBlock.nonce = Integer.toString(Integer.parseInt(newBlock.nonce)+1);
        newBlock.dgst = ob.Fn(start_string+ "#" + newBlock.trsummary+ "#"+ newBlock.nonce);
      }
    }
    else
    {  newBlock.dgst = ob.Fn(last.dgst+ "#" + newBlock.trsummary+ "#"+ newBlock.nonce);
      while(!newBlock.dgst.substring(0,4).equals("0000"))
      {
        newBlock.nonce = Integer.toString(Integer.parseInt(newBlock.nonce)+1);
        newBlock.dgst = ob.Fn(last.dgst+ "#" + newBlock.trsummary+ "#"+ newBlock.nonce);
      }
    }
    newBlock.previous = last;
    ///sdnhsidyhiS
    int p=0;
    while(lastBlocksList[p]!=null)
    {
      //System.out.println(p + " " + lastBlocksList[p]);
      p++;
    }
    //System.out.println(p);
    int i =0;
    while(i<p)
    {
      if(lastBlocksList[i]==this.FindLongestValidChain())
      {
        lastBlocksList[i] = newBlock;
        break;
      }
      i++;
    }
    if(i==p)
    {
      lastBlocksList[i] = newBlock;
    }
  
  }
}
