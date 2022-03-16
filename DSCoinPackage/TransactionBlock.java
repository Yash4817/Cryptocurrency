package DSCoinPackage;

import HelperClasses.MerkleTree;
import HelperClasses.CRF;

public class TransactionBlock {

  public Transaction[] trarray;
  public TransactionBlock previous;
  public MerkleTree Tree;
  public String trsummary;
  public String nonce;
  public String dgst;
  TransactionBlock(){}

  public TransactionBlock(Transaction[] t) {
    trarray = new Transaction[t.length];
    for(int i=0;i<t.length;i++)
    {
      this.trarray[i] = t[i];
      //System.out.println(trarray[i].coinID + " and " + trarray[i].Source.UID + " and " + trarray[i].Destination.UID );
    }
    this.previous = null;
    MerkleTree l = new MerkleTree();
    //System.out.println("out");
    this.trsummary = l.Build(trarray);
    //System.out.println("out2");
    Tree = l;
    this.dgst = null;
    //System.out.println("out3");
  }

  public boolean checkTransaction (Transaction t) {
    int firstcond = 0;
    int x=0;
    if(t.coinsrc_block == null)
    {
      firstcond = 1;
    }
    else
    {
      for(int i=0;i<t.coinsrc_block.trarray.length;i++)
      {
        if(t.coinsrc_block.trarray[i].coinID.equals(t.coinID))
        {
          x++;
        }
        if(t.coinsrc_block.trarray[i].coinID.equals(t.coinID) && t.coinsrc_block.trarray[i].Destination == t.Source)
        {
          firstcond = 1;
        }
      }
    }
    int secondcond=0;
    TransactionBlock temp = this.previous; 
    // for(int i=0;i<temp.trarray.length;i++)
    //   {
    //     if(temp.trarray[i].coinID == t.coinID)
    //     {
    //       secondcond++;
    //     }
    //   }
    while(temp != t.coinsrc_block)
    {
      for(int i=0;i<temp.trarray.length;i++)
      {
        if(temp.trarray[i].coinID.equals(t.coinID))
        {
          secondcond++;
        }
      }
      temp = temp.previous;
    }
    if(firstcond == 1 && secondcond==0 && x<2 )
    {
      return true;
    }
    return false;
  
  }
}
