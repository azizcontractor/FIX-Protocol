# FIX-Protocol
<p>The goal was to learn FIX protocol version 5.0 and use it in programming a client/server application. FIX stands for Financial Information eXchange which is an open specification originally developed for institutions to send and receive financial information. It has become a standard in securities trading. </p>
<p>In this programming assignmenent I wrote a client application and a multithreaded server application using Java programming language. Both the clients and the server are run from the command line (or terminal). </p>
<p>This program pair simulates the brokerage firms sending order to a market center (such as an exchange) which executes the order based on market condition. </p>
The following types of actions can be taken from the clients:
<ul>
     <li>Order – New Order Single D</li>
     <li>Order Cancel Request F</li>
     <li>Order Cancel Replace Request G</li>
     <li>Order Status Request H</li>
     <li>Quote Request R</li>
</ul>
The following types of actions can be taken from the server:
<ul>
     <li>Quote S</li>
     <li>Execution Report '<8>'
        <ul>
        <li>Cancel Reject 8</li>
        <li>Cancelled 4</li>
        <li>Replace 5</li>
        <li>Expired C</li>
        <li>Pending New A</li>
        </ul>
        </li>
</ul>
