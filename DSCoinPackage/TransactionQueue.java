package DSCoinPackage;
import java.util.*;
import java.io.*;
public class TransactionQueue {

  public Transaction firstTransaction;
  public Transaction lastTransaction;
  public int numTransactions;
  public List<Transaction> q = new ArrayList<>();
  public void AddTransactions (Transaction transaction) {
    if(numTransactions==0)
    {
      q.add(transaction);
      firstTransaction = q.get(0);
      lastTransaction = q.get(0);
      numTransactions++;
      return;
    }
    q.add(transaction);
    lastTransaction = transaction;
    numTransactions++;
    return;
  }
  
  public Transaction RemoveTransaction () throws EmptyQueueException {
    Transaction dequed = new Transaction();
    if(numTransactions==0)
    {
      throw new EmptyQueueException();
    }
    else
    {
      dequed = q.get(0);
      q.remove(0);
      if(q.size()!=0){
      firstTransaction = q.get(0);
      }
      else{
        firstTransaction = null;
        lastTransaction = null;
      }
      numTransactions--;
      
    }
    return dequed;
  }

  public int size() {
    return numTransactions; 
  }
}
