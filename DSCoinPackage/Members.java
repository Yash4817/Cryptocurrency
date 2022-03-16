package DSCoinPackage;

import java.util.*;
import HelperClasses.*;

public class Members
 {

  public String UID;
  public List<Pair<String, TransactionBlock>> mycoins;
  public Transaction[] in_process_trans;
  public void reverse(List<Pair<String,String>> list)
  {
    if(list.size()<=1 || list==null)
    {
      return;
    }
    Pair<String,String> p = list.remove(0);
    reverse(list);
    list.add(p);
  }
  public List<Pair<String,TransactionBlock>> Sort(List<Pair<String,TransactionBlock>> p)
  {
    int l = p.size();
		if(l==1){
			return p;
		}
    for(int i=0;i<l;i++)
			{
				for(int j=0;j<l-i-1;j++)
				{
					if(Integer.parseInt(p.get(j).get_first()) > Integer.parseInt(p.get(j+1).get_first()))
					{
						Pair<String,TransactionBlock> temp = p.get(j);
						p.set(j,p.get(j+1));
						p.set(j+1,temp);
					}
				}
			}
      return p;
  }
  public List<Pair<String,String>> QueryDocument(int doc_idx,TransactionBlock tB)
  {
    int num_trans = tB.trarray.length;
		int t = (int)(Math.log(num_trans)/Math.log(2));
    //System.out.println(num_trans);
		int temp = doc_idx;
		ArrayList<Integer> path = new ArrayList<>();
		path.add(temp);
		for(int i=0;i<t-1;i++){
			if(temp % 2 ==0){
				temp = temp/2;
				path.add(temp);
			}
			else{
				temp = (temp-1)/2;
				path.add(temp);
			}
		}
		int j = path.size();
		ArrayList<Pair<String,String>> p = new ArrayList<>();
		TreeNode curr = tB.Tree.rootnode;
/*		p.add(new Pair<String,String>(curr.val,null));
		for(int i=0;i<j;i++){
			p.add(new Pair<String,String>(curr.left.val,curr.right.val));
			if(path.get(j-i-1)%2==0){
				curr = curr.right;
			}else{
				curr = curr.left;
			}
		}
*/		
		for(int i=0;i<j;i++){
			if(path.get(j-i-1)%2==0){
                                curr = curr.left;
                        }else{
                                curr = curr.right;
                        }

		}
		for(int i=0;i<j;i++){
			curr = curr.parent;
			p.add(new Pair<String,String>(curr.left.val,curr.right.val)); 
		}
		p.add(new Pair<String,String>(curr.val,null));
		return p;
      	}


  
  public void remove(Transaction t)
  {
    int j=0;
    int i=0;
    Transaction[] arr = new Transaction[this.in_process_trans.length];
    for( i=0;i<this.in_process_trans.length;i++)
    {
      if(in_process_trans[i]!=t)
      {
        arr[j] = in_process_trans[i];
        j++;
      }
    }
    for(i=0;i<j;i++)
    {
      in_process_trans[i] = arr[i];
    }
    for(;i<in_process_trans.length;i++)
    {
      in_process_trans[i] = null;
    }
  }
  public boolean checkT(Transaction t,DSCoin_Honest DSObj)
  {
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
    TransactionBlock temp = DSObj.bChain.lastBlock; 
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
  public boolean checkT(Transaction t,DSCoin_Malicious DSObj)
  {
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
    TransactionBlock temp = DSObj.bChain.FindLongestValidChain(); 
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
  
  public void initiateCoinsend(String destUID, DSCoin_Honest DSobj)
  {
    Pair<String,TransactionBlock> first = mycoins.get(0);
    mycoins.remove(0);
    Transaction tobj = new Transaction();
    tobj.coinID = first.get_first();
    //System.out.println(tobj.coinID + " and "+ first.get_first());
    tobj.Source = this;
    for(int i =0;i<DSobj.memberlist.length;i++)
    {
      if(DSobj.memberlist[i].UID.equals(destUID))
        tobj.Destination = DSobj.memberlist[i];
    }
    //jhyuujh
    tobj.coinsrc_block = first.get_second();
    int j = 0;
    while(in_process_trans[j]!=null)
    {
      j++;
    }
    in_process_trans[j]=tobj;
    DSobj.pendingTransactions.AddTransactions(tobj);
    return;
  }

  public void initiateCoinsend(String destUID, DSCoin_Malicious DSobj) {
    Pair<String,TransactionBlock> first = mycoins.get(0);
    mycoins.remove(0);
    Transaction tobj = new Transaction();
    tobj.coinID = first.get_first();
    tobj.Source = this;
    for(int i =0;i<DSobj.memberlist.length;i++)
    {
      if(DSobj.memberlist[i].UID.equals(destUID))
        tobj.Destination = DSobj.memberlist[i];
    }
    tobj.coinsrc_block = first.get_second();
    int j = 0;
    while(in_process_trans[j]!=null)
    {
      j++;
    }
    in_process_trans[j]=tobj;
    DSobj.pendingTransactions.AddTransactions(tobj);
    return;
  }

  public Pair<List<Pair<String, String>>, List<Pair<String, String>>> finalizeCoinsend (Transaction tobj, DSCoin_Honest DSObj) throws MissingTransactionException {
    TransactionBlock curr = DSObj.bChain.lastBlock;
    int j =0;
    int i=0;
    while(curr != null)
    {
      for(;i<curr.trarray.length;i++)
      {
        if(curr.trarray[i] == tobj)
        {
          j=1;
          break;
        }
      }
      if(j==1)
      {
        break;
      }
      curr = curr.previous;
    }
    if(curr==null)
    {
      throw new MissingTransactionException();
    }
    //System.out.println("ok");
    List<Pair<String,String>> p1 = new ArrayList<>();
    p1 = QueryDocument(i,curr);
    List<Pair<String,String>> p2 = new ArrayList<>();
    TransactionBlock temp = DSObj.bChain.lastBlock;
    
    while(temp != curr )
    {
      p2.add(new Pair<String,String>(temp.dgst,temp.previous.dgst+"#"+temp.trsummary+"#"+temp.nonce));
      temp = temp.previous;
    }
    p2.add(new Pair<String,String>(temp.dgst,temp.previous.dgst+"#"+temp.trsummary+"#"+temp.nonce));
    p2.add(new Pair<String,String>(temp.previous.dgst,null));
    reverse(p2);
    this.remove(tobj); 
    tobj.Destination.mycoins.add(new Pair<String,TransactionBlock>(tobj.coinID,curr));//cffdffdhkhgukhu
    tobj.Destination.mycoins = Sort(tobj.Destination.mycoins);
    return new Pair<List<Pair<String,String>>,List<Pair<String,String>>>(p1,p2);
  }

  public void MineCoin(DSCoin_Honest DSObj) 
  {
    //Transaction[] arr = new Transaction[DSObj.bChain.tr_count];
    int x=0,y=0;
    List<Transaction> valid = new ArrayList<>();
    List<Transaction> unvalid = new ArrayList<>();
    int i=0;
    while(DSObj.pendingTransactions.firstTransaction != null && valid.size()< DSObj.bChain.tr_count)
    {
      //TransactionBlock obj = new TransactionBlock(); 
      //System.out.println(DSObj.pendingTransactions.numTransactions + " " + valid.size() + " " + unvalid.size());
      //System.out.println(DSObj.pendingTransactions.firstTransaction.coinID+ " "+DSObj.pendingTransactions.firstTransaction);
      if(checkT(DSObj.pendingTransactions.firstTransaction,DSObj))//fxgfhfdhgdgngfnfhfmhmfncvxzxxsac vbvmmnvnmbvv xnb;
      {
        y=1;
        
        //arr.add(DSObj.pendingTransactions.RemoveTransaction());
  
        // for(i=0;i<valid.size() ;i++)
        // {
        //   if(DSObj.pendingTransactions.firstTransaction.coinID.equals(valid.get(i).coinID))
        //   {
        //     try{
        //       unvalid.add(DSObj.pendingTransactions.RemoveTransaction());
        //       System.out.println("here");
        //     } catch(Exception e){
        //       System.out.println("error1");
        //     } 
        //     x=1;
        //     break;
        //   }
        // }
        if(valid.contains(DSObj.pendingTransactions.firstTransaction))
        {
          try{
            unvalid.add(DSObj.pendingTransactions.RemoveTransaction());
            //System.out.println("here");
            } catch(Exception e){
            System.out.println("error1");
            }
        }
        else
        {
          try{
            valid.add(DSObj.pendingTransactions.RemoveTransaction());
          } catch(Exception e){
            System.out.println("error2"+e);
          } 
        }

      }
      if(y==0)
      {
        try{
          unvalid.add(DSObj.pendingTransactions.RemoveTransaction());
        } catch(Exception e){
          System.out.println("error3");
        } 
      }
      y=0;
      x=0;
    }
    Transaction[] arr = new Transaction[valid.size()+1];
    for(i=0;i<valid.size();i++)
    {
      arr[i] = valid.get(i);
    }
    Transaction minerRewardTransaction = new Transaction();
    String newCoinID = Integer.toString(Integer.parseInt(DSObj.latestCoinID)+1);
    minerRewardTransaction.coinID = newCoinID;
    minerRewardTransaction.Source = null;
    minerRewardTransaction.Destination = this;
    minerRewardTransaction.coinsrc_block = null;
    arr[i] = minerRewardTransaction;
    TransactionBlock tB = new TransactionBlock(arr);
    DSObj.bChain.InsertBlock_Honest(tB);
    this.mycoins.add(new Pair<String,TransactionBlock>(newCoinID,tB));//cffgyfasjygkugku
    this.mycoins = Sort(this.mycoins);
    DSObj.latestCoinID = newCoinID;
  }  

  public void MineCoin(DSCoin_Malicious DSObj) {
    int x=0,y=0;
    List<Transaction> valid = new ArrayList<>();
    List<Transaction> unvalid = new ArrayList<>();
    int i=0;
    while(DSObj.pendingTransactions.firstTransaction != null && valid.size()< DSObj.bChain.tr_count)
    {
      //TransactionBlock obj = new TransactionBlock(); 
      if(checkT(DSObj.pendingTransactions.firstTransaction,DSObj))
      {
        y=1;
        
        //arr.add(DSObj.pendingTransactions.RemoveTransaction());
        // for(i=0;i<valid.size() ;i++)
        // {
        //   if(DSObj.pendingTransactions.firstTransaction.coinID.equals(valid.get(i).coinID))
        //   {
        //     try{
        //       unvalid.add(DSObj.pendingTransactions.RemoveTransaction());
        //     } catch(Exception e){
        //       System.out.println("error4");
        //     } 
        //     x=1;
        //     break;
        //   }
        // }
        // if(x==0 )
        // {
        //   try{
        //     valid.add(DSObj.pendingTransactions.RemoveTransaction());
        //   } catch(Exception e){
        //     System.out.println("error5");
        //   } 
        // }
        if(valid.contains(DSObj.pendingTransactions.firstTransaction))
        {
          try{
            unvalid.add(DSObj.pendingTransactions.RemoveTransaction());
            //System.out.println("here");
            } catch(Exception e){
            System.out.println("error1");
            }
        }
        else
        {
          try{
            valid.add(DSObj.pendingTransactions.RemoveTransaction());
          } catch(Exception e){
            System.out.println("error2"+e);
          } 
        }

      }
      if(y==0)
      {
        try{
          unvalid.add(DSObj.pendingTransactions.RemoveTransaction());
        } catch(Exception e){
          System.out.println("error6");
        } 
      }
      y=0;
      x=0;
    }
    Transaction[] arr = new Transaction[valid.size()+1];
    for(i=0;i<valid.size();i++)
    {
      arr[i] = valid.get(i);
    }
    Transaction minerRewardTransaction = new Transaction();
    String newCoinID = Integer.toString(Integer.parseInt(DSObj.latestCoinID)+1);
    minerRewardTransaction.coinID = newCoinID;
    minerRewardTransaction.Source = null;
    minerRewardTransaction.Destination = this;
    minerRewardTransaction.coinsrc_block = null;
    arr[i]=minerRewardTransaction;
    TransactionBlock tB = new TransactionBlock(arr);
    DSObj.bChain.InsertBlock_Malicious(tB);
    this.mycoins.add(new Pair<String,TransactionBlock>(newCoinID,tB));//ctfgufyryrsrdhgkg
    this.mycoins = Sort(this.mycoins);
    DSObj.latestCoinID = newCoinID;
  }  
}
