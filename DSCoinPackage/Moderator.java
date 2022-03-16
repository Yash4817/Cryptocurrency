package DSCoinPackage;
import java.util.*;
import HelperClasses.*;

public class Moderator
 {

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
  public void initializeDSCoin(DSCoin_Honest DSObj, int coinCount) {
    String id = "100000";
    int j=0;
    int x = coinCount/DSObj.bChain.tr_count;//hyftt
    int i=0;
    int k=0;
    Transaction[] tr = new Transaction[coinCount];
    // ArrayList<Transaction> tr = new ArrayList<>();
    while(j < coinCount)
    { 
      if(i==DSObj.memberlist.length)
        i=0;
      Members m = new Members();
      m.UID = "Moderator";
      Transaction p = new Transaction();
      p.coinID = id;
      p.Source = m;
      p.Destination = DSObj.memberlist[i];
      p.coinsrc_block = null;
      tr[j] = p;
      //DSObj.pendingTransactions.AddTransactions(tr[j]);
      //DSObj.memberlist[i].mycoins.first = id;
      DSObj.latestCoinID = id;
      id = Integer.toString(Integer.parseInt(id)+1);
      //System.out.println(tr[j].coinID);
      j++;
      i++;
    }

    j=0;
    int l=0;
    String cid = "100000";
    for(i=0;i<x;i++)
    {
      Transaction[] arr = new Transaction[DSObj.bChain.tr_count];
      for(k=0;k<DSObj.bChain.tr_count;k++)
      {
        arr[k] = tr[j];
        j++;
      }
      TransactionBlock b = new TransactionBlock(arr);
      DSObj.bChain.InsertBlock_Honest(b);    
      //System.out.println(b.dgst);
      for(k=0;k<DSObj.bChain.tr_count;k++)
      {
        if(l==DSObj.memberlist.length)
        l=0;
        DSObj.memberlist[l].mycoins.add(new Pair<String,TransactionBlock>(cid,DSObj.bChain.lastBlock));//.second = DSObj.bChain.lastBlock;
        DSObj.memberlist[l].mycoins = Sort(DSObj.memberlist[l].mycoins);
        //System.out.println(DSObj.memberlist[l].mycoins.get(DSObj.memberlist[l].mycoins.size()-1).first + " and " + DSObj.memberlist[l].mycoins.get(DSObj.memberlist[l].mycoins.size()-1).second + " and " +l);
        cid = Integer.toString(Integer.parseInt(cid)+1);
        l++;
      }
    }
  }
    
  public void initializeDSCoin(DSCoin_Malicious DSObj, int coinCount) {
    String id = "100000";
    int j=0;
    int x = coinCount/DSObj.bChain.tr_count;//hyftt
    int i=0;
    int k=0;
    Transaction[] tr = new Transaction[coinCount];
    while(j<coinCount)
    { if(i==DSObj.memberlist.length)
        i=0;
      Members m = new Members();
      m.UID = "Moderator";
      Transaction p = new Transaction();
      p.coinID = id;
      p.Source = m;
      p.Destination = DSObj.memberlist[i];
      p.coinsrc_block = null;
      tr[j] = p;
      //DSObj.pendingTransactions.AddTransactions(tr[j]);
      //DSObj.memberlist[i].mycoins.first = id;
      DSObj.latestCoinID = id;
      id = Integer.toString(Integer.parseInt(id)+1);
      j++;
      i++;
    }
    j=0;
    int l=0;
    for(i=0;i<x;i++)
    {
      Transaction[] arr = new Transaction[DSObj.bChain.tr_count];
      for(k=0;k<DSObj.bChain.tr_count;k++)
      {
        arr[k] = tr[j];
        j++;
      }
      TransactionBlock b = new TransactionBlock(arr);
      DSObj.bChain.InsertBlock_Malicious(b);    
      String cid = "100000";
      for(k=0;k<DSObj.bChain.tr_count;k++)
      {
        if(l==DSObj.memberlist.length)
        l=0;
        DSObj.memberlist[l].mycoins.add(new Pair<String,TransactionBlock>(cid,DSObj.bChain.FindLongestValidChain()));//.second = DSObj.bChain.FindLongestValidChain();
        DSObj.memberlist[l].mycoins = Sort(DSObj.memberlist[l].mycoins);
        cid = Integer.toString(Integer.parseInt(cid)+1);
        l++;
      }
    }
  }
}
