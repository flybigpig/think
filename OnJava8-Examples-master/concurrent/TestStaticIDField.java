// concurrent/TestStaticIDField.java
// (c)2017 MindView LLC: see Copyright.txt
// We make no guarantees that this code is fit for any purpose.
// Visit http://OnJava8.com for more book information.

public class TestStaticIDField {
  public static void main(String[] args) {
    IDChecker.test(StaticIDField::new);
  }
}
/* Output:
13287
*/
