package DSCoinPackage;

import HelperClasses.CRF;

public class BlockChain_Honest {

  public int tr_count;//dsjdhuhjcjivjzvkzvkzvzkvkzjcajcaca
  public static final String start_string = "DSCoin";
  public TransactionBlock lastBlock;

  public void InsertBlock_Honest (TransactionBlock newBlock)
  {
    CRF ob = new CRF(64);
    newBlock.nonce = "1000000001";
    if(lastBlock==null)
    {
      
      newBlock.dgst = ob.Fn(start_string+ "#" + newBlock.trsummary+ "#"+ newBlock.nonce);
      while(!newBlock.dgst.substring(0,4).equals("0000"))
      {
        newBlock.nonce = Integer.toString(Integer.parseInt(newBlock.nonce)+1);
        newBlock.dgst = ob.Fn(start_string+ "#" + newBlock.trsummary+ "#"+ newBlock.nonce);
        //System.out.println(newBlock.trsummary + " and " + newBlock.nonce +" and " + newBlock.dgst.substring(0,4));
      }
    }
    else
    {  newBlock.dgst = ob.Fn(lastBlock.dgst+ "#" + newBlock.trsummary+ "#"+ newBlock.nonce);
      while(!newBlock.dgst.substring(0,4).equals("0000"))
      {
        newBlock.nonce = Integer.toString(Integer.parseInt(newBlock.nonce)+1);
        newBlock.dgst = ob.Fn(lastBlock.dgst+ "#" + newBlock.trsummary+ "#"+ newBlock.nonce);
      }
    }
    newBlock.previous = lastBlock;
    lastBlock = newBlock;
    }
}
