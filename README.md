# FIX-Protocol
The goal was to learn FIX protocol version 5.0 and use it in programming a client/server application. FIX stands for Financial Information eXchange which is an open specification originally developed for institutions to send and receive financial information. It has become a standard in securities trading. 
In this programming assignmenent I wrote a client application and a multithreaded server application using Java programming language. Both the clients and the server are run from the command line (or terminal). 
This program pair simulates the brokerage firms sending order to a market center (such as an exchange) which executes the order based on market condition. 
The following types of actions can be taken from the clients:
     Order – New Order Single <D>
     Order Cancel Request <F>
     Order Cancel Replace Request <G>
     Order Status Request <H>
     Quote Request <R>
The following types of actions can be taken from the server:
     Quote <S>
     Execution Report <8>
        Cancel Reject <8>
        Cancelled <4>
        Replace <5>
        Expired <C>
        Pending New <A>
