# Email-Spammer-Java
## A tool to send multiple emails, written in Java

###1. Compiling:

```
javac -cp javax.mail.jar SendEmailTLS.java
```

###2. Running

On Windows: 
```java -cp .;javax.mail.jar SendEmailTLS senders.txt receivers.txt content.txt```

On Linux: 
```java -cp .:javax.mail.jar SendEmailTLS senders.txt receivers.txt content.txt```

Where:
*senders.txt* is the file that contains all sender emails
*receivers.txt* is the file that contains all receiver emails
*content.txt* is the content of the email we want to broadcast

Attention:
- Each sender will send 140 emails, after it's the turn the next sender. (the number 140 can be modified in the code).
- The log.txt is the log file
- The maximum number of senders is 10.



