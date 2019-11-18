package pa2;

// Import declarations generated by the SALSA compiler, do not modify.
import java.io.IOException;
import java.util.Vector;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.InvocationTargetException;

import salsa.language.Actor;
import salsa.language.ActorReference;
import salsa.language.Message;
import salsa.language.RunTime;
import salsa.language.ServiceFactory;
import gc.WeakReference;
import salsa.language.Token;
import salsa.language.exceptions.*;
import salsa.language.exceptions.CurrentContinuationException;

import salsa.language.UniversalActor;

import salsa.naming.UAN;
import salsa.naming.UAL;
import salsa.naming.MalformedUALException;
import salsa.naming.MalformedUANException;

import salsa.resources.SystemService;

import salsa.resources.ActorService;

// End SALSA compiler generated import delcarations.

import java.io.*;
import java.util.*;
import java.lang.Math;

public class Main extends UniversalActor  {
	public static void main(String args[]) {
		UAN uan = null;
		UAL ual = null;
		if (System.getProperty("uan") != null) {
			uan = new UAN( System.getProperty("uan") );
			ServiceFactory.getTheater();
			RunTime.receivedUniversalActor();
		}
		if (System.getProperty("ual") != null) {
			ual = new UAL( System.getProperty("ual") );

			if (uan == null) {
				System.err.println("Actor Creation Error:");
				System.err.println("	uan: " + uan);
				System.err.println("	ual: " + ual);
				System.err.println("	Identifier: " + System.getProperty("identifier"));
				System.err.println("	Cannot specify an actor to have a ual at runtime without a uan.");
				System.err.println("	To give an actor a specific ual at runtime, use the identifier system property.");
				System.exit(0);
			}
			RunTime.receivedUniversalActor();
		}
		if (System.getProperty("identifier") != null) {
			if (ual != null) {
				System.err.println("Actor Creation Error:");
				System.err.println("	uan: " + uan);
				System.err.println("	ual: " + ual);
				System.err.println("	Identifier: " + System.getProperty("identifier"));
				System.err.println("	Cannot specify an identifier and a ual with system properties when creating an actor.");
				System.exit(0);
			}
			ual = new UAL( ServiceFactory.getTheater().getLocation() + System.getProperty("identifier"));
		}
		RunTime.receivedMessage();
		Main instance = (Main)new Main(uan, ual,null).construct();
		gc.WeakReference instanceRef=new gc.WeakReference(uan,ual);
		{
			Object[] _arguments = { args };

			//preAct() for local actor creation
			//act() for remote actor creation
			if (ual != null && !ual.getLocation().equals(ServiceFactory.getTheater().getLocation())) {instance.send( new Message(instanceRef, instanceRef, "act", _arguments, false) );}
			else {instance.send( new Message(instanceRef, instanceRef, "preAct", _arguments, false) );}
		}
		RunTime.finishedProcessingMessage();
	}

	public static ActorReference getReferenceByName(UAN uan)	{ return new Main(false, uan); }
	public static ActorReference getReferenceByName(String uan)	{ return Main.getReferenceByName(new UAN(uan)); }
	public static ActorReference getReferenceByLocation(UAL ual)	{ return new Main(false, ual); }

	public static ActorReference getReferenceByLocation(String ual)	{ return Main.getReferenceByLocation(new UAL(ual)); }
	public Main(boolean o, UAN __uan)	{ super(false,__uan); }
	public Main(boolean o, UAL __ual)	{ super(false,__ual); }
	public Main(UAN __uan,UniversalActor.State sourceActor)	{ this(__uan, null, sourceActor); }
	public Main(UAL __ual,UniversalActor.State sourceActor)	{ this(null, __ual, sourceActor); }
	public Main(UniversalActor.State sourceActor)		{ this(null, null, sourceActor);  }
	public Main()		{  }
	public Main(UAN __uan, UAL __ual, Object obj) {
		//decide the type of sourceActor
		//if obj is null, the actor must be the startup actor.
		//if obj is an actorReference, this actor is created by a remote actor

		if (obj instanceof UniversalActor.State || obj==null) {
			  UniversalActor.State sourceActor;
			  if (obj!=null) { sourceActor=(UniversalActor.State) obj;}
			  else {sourceActor=null;}

			  //remote creation message sent to a remote system service.
			  if (__ual != null && !__ual.getLocation().equals(ServiceFactory.getTheater().getLocation())) {
			    WeakReference sourceRef;
			    if (sourceActor!=null && sourceActor.getUAL() != null) {sourceRef = new WeakReference(sourceActor.getUAN(),sourceActor.getUAL());}
			    else {sourceRef = null;}
			    if (sourceActor != null) {
			      if (__uan != null) {sourceActor.getActorMemory().getForwardList().putReference(__uan);}
			      else if (__ual!=null) {sourceActor.getActorMemory().getForwardList().putReference(__ual);}

			      //update the source of this actor reference
			      setSource(sourceActor.getUAN(), sourceActor.getUAL());
			      activateGC();
			    }
			    createRemotely(__uan, __ual, "pa2.Main", sourceRef);
			  }

			  // local creation
			  else {
			    State state = new State(__uan, __ual);

			    //assume the reference is weak
			    muteGC();

			    //the source actor is  the startup actor
			    if (sourceActor == null) {
			      state.getActorMemory().getInverseList().putInverseReference("rmsp://me");
			    }

			    //the souce actor is a normal actor
			    else if (sourceActor instanceof UniversalActor.State) {

			      // this reference is part of garbage collection
			      activateGC();

			      //update the source of this actor reference
			      setSource(sourceActor.getUAN(), sourceActor.getUAL());

			      /* Garbage collection registration:
			       * register 'this reference' in sourceActor's forward list @
			       * register 'this reference' in the forward acquaintance's inverse list
			       */
			      String inverseRefString=null;
			      if (sourceActor.getUAN()!=null) {inverseRefString=sourceActor.getUAN().toString();}
			      else if (sourceActor.getUAL()!=null) {inverseRefString=sourceActor.getUAL().toString();}
			      if (__uan != null) {sourceActor.getActorMemory().getForwardList().putReference(__uan);}
			      else if (__ual != null) {sourceActor.getActorMemory().getForwardList().putReference(__ual);}
			      else {sourceActor.getActorMemory().getForwardList().putReference(state.getUAL());}

			      //put the inverse reference information in the actormemory
			      if (inverseRefString!=null) state.getActorMemory().getInverseList().putInverseReference(inverseRefString);
			    }
			    state.updateSelf(this);
			    ServiceFactory.getNaming().setEntry(state.getUAN(), state.getUAL(), state);
			    if (getUAN() != null) ServiceFactory.getNaming().update(state.getUAN(), state.getUAL());
			  }
		}

		//creation invoked by a remote message
		else if (obj instanceof ActorReference) {
			  ActorReference sourceRef= (ActorReference) obj;
			  State state = new State(__uan, __ual);
			  muteGC();
			  state.getActorMemory().getInverseList().putInverseReference("rmsp://me");
			  if (sourceRef.getUAN() != null) {state.getActorMemory().getInverseList().putInverseReference(sourceRef.getUAN());}
			  else if (sourceRef.getUAL() != null) {state.getActorMemory().getInverseList().putInverseReference(sourceRef.getUAL());}
			  state.updateSelf(this);
			  ServiceFactory.getNaming().setEntry(state.getUAN(), state.getUAL(),state);
			  if (getUAN() != null) ServiceFactory.getNaming().update(state.getUAN(), state.getUAL());
		}
	}

	public UniversalActor construct() {
		Object[] __arguments = { };
		this.send( new Message(this, this, "construct", __arguments, null, null) );
		return this;
	}

	public class State extends UniversalActor .State {
		public Main self;
		public void updateSelf(ActorReference actorReference) {
			((Main)actorReference).setUAL(getUAL());
			((Main)actorReference).setUAN(getUAN());
			self = new Main(false,getUAL());
			self.setUAN(getUAN());
			self.setUAL(getUAL());
			self.activateGC();
		}

		public void preAct(String[] arguments) {
			getActorMemory().getInverseList().removeInverseReference("rmsp://me",1);
			{
				Object[] __args={arguments};
				self.send( new Message(self,self, "act", __args, null,null,false) );
			}
		}

		public State() {
			this(null, null);
		}

		public State(UAN __uan, UAL __ual) {
			super(__uan, __ual);
			addClassName( "pa2.Main$State" );
			addMethodsForClasses();
		}

		public void construct() {}

		public void process(Message message) {
			Method[] matches = getMatches(message.getMethodName());
			Object returnValue = null;
			Exception exception = null;

			if (matches != null) {
				if (!message.getMethodName().equals("die")) {activateArgsGC(message);}
				for (int i = 0; i < matches.length; i++) {
					try {
						if (matches[i].getParameterTypes().length != message.getArguments().length) continue;
						returnValue = matches[i].invoke(this, message.getArguments());
					} catch (Exception e) {
						if (e.getCause() instanceof CurrentContinuationException) {
							sendGeneratedMessages();
							return;
						} else if (e instanceof InvocationTargetException) {
							sendGeneratedMessages();
							exception = (Exception)e.getCause();
							break;
						} else {
							continue;
						}
					}
					sendGeneratedMessages();
					currentMessage.resolveContinuations(returnValue);
					return;
				}
			}

			System.err.println("Message processing exception:");
			if (message.getSource() != null) {
				System.err.println("\tSent by: " + message.getSource().toString());
			} else System.err.println("\tSent by: unknown");
			System.err.println("\tReceived by actor: " + toString());
			System.err.println("\tMessage: " + message.toString());
			if (exception == null) {
				if (matches == null) {
					System.err.println("\tNo methods with the same name found.");
					return;
				}
				System.err.println("\tDid not match any of the following: ");
				for (int i = 0; i < matches.length; i++) {
					System.err.print("\t\tMethod: " + matches[i].getName() + "( ");
					Class[] parTypes = matches[i].getParameterTypes();
					for (int j = 0; j < parTypes.length; j++) {
						System.err.print(parTypes[j].getName() + " ");
					}
					System.err.println(")");
				}
			} else {
				System.err.println("\tThrew exception: " + exception);
				exception.printStackTrace();
			}
		}

		String theatersFile = "";
		String nameServer = "127.0.1.1:3030";
		boolean isDebug = true;
		Vector nodes = new Vector();
		int numNodes;
		public void act(String args[]) {
			int argc = args.length;
			if (argc>=1) {theatersFile = args[0];
}			if (argc>=2) {nameServer = args[1];
}			if (argc>=3) {isDebug = Boolean.parseBoolean(args[2]);
}			if (isDebug) {			{
				// standardOutput<-print("Enter n: ")
				{
					Object _arguments[] = { "Enter n: " };
					Message message = new Message( self, standardOutput, "print", _arguments, null, null );
					__messages.add( message );
				}
			}
}			Token n = new Token("n");
			{
				// token n = standardInput<-readLine()
				{
					Object _arguments[] = {  };
					Message message = new Message( self, standardInput, "readLine", _arguments, null, n );
					__messages.add( message );
				}
			}
			{
				Token token_2_0 = new Token();
				// createDHT(n)
				{
					Object _arguments[] = { n };
					Message message = new Message( self, self, "createDHT", _arguments, null, token_2_0 );
					__messages.add( message );
				}
				// checkForInput()
				{
					Object _arguments[] = {  };
					Message message = new Message( self, self, "checkForInput", _arguments, token_2_0, null );
					__messages.add( message );
				}
			}
		}
		public void createDHT(String n_string) {
			int n = Integer.parseInt(n_string);
			numNodes = (int)Math.pow(2, n);
			Vector theaters = new Vector();
			String theater;
			if (theatersFile!="") {{
				{
					// standardOutput<-println("Valid Theaters File")
					{
						Object _arguments[] = { "Valid Theaters File" };
						Message message = new Message( self, standardOutput, "println", _arguments, null, null );
						__messages.add( message );
					}
				}
				try {
					BufferedReader in = new BufferedReader(new FileReader(theatersFile));
					while ((theater=in.readLine())!=null) {
						theaters.add(theater);
					}
					in.close();
				}
				catch (IOException ioe) {
					{
						// standardError<-println("[error] Can't open the file "+theatersFile+" for reading. Will create DHT nodes locally.")
						{
							Object _arguments[] = { "[error] Can't open the file "+theatersFile+" for reading. Will create DHT nodes locally." };
							Message message = new Message( self, standardError, "println", _arguments, null, null );
							__messages.add( message );
						}
					}
					theatersFile = "";
				}

			}
}			if (theatersFile!="") {{
				{
					// standardOutput<-println("Valid Theaters File")
					{
						Object _arguments[] = { "Valid Theaters File" };
						Message message = new Message( self, standardOutput, "println", _arguments, null, null );
						__messages.add( message );
					}
				}
				String[] uans = new String[numNodes];
				for (int i = 0; i<numNodes; i++){
					String uan_str = "uan://"+nameServer+"/id"+i;
					String ual_str = "rmsp://"+theaters.get(i%theaters.size())+"/id"+i;
					if (isDebug) {{
						{
							// standardOutput<-println("Creating node "+uan_str+" at "+ual_str)
							{
								Object _arguments[] = { "Creating node "+uan_str+" at "+ual_str };
								Message message = new Message( self, standardOutput, "println", _arguments, null, null );
								__messages.add( message );
							}
						}
					}
}					Node nd = ((Node)new Node(new UAN(uan_str), new UAL(ual_str),this).construct(i, n, ((Main)self)));
					nodes.add(nd);
					uans[i] = uan_str;
				}
				for (int i = 0; i<numNodes; i++){
					for (int j = 0; j<n; j++){
						int connectingNodeId = ((int)(i+Math.pow(2, j)))%(int)Math.pow(2, n);
						{
							// standardOutput<-println("Connection from "+i+" to "+connectingNodeId)
							{
								Object _arguments[] = { "Connection from "+i+" to "+connectingNodeId };
								Message message = new Message( self, standardOutput, "println", _arguments, null, null );
								__messages.add( message );
							}
						}
						Node nd = (Node)nodes.get(i);
						Node nd_connect = (Node)nodes.get(connectingNodeId);
						{
							// nd<-addConnection(nd_connect, connectingNodeId)
							{
								Object _arguments[] = { nd_connect, connectingNodeId };
								Message message = new Message( self, nd, "addConnection", _arguments, null, null );
								__messages.add( message );
							}
						}
					}
				}
			}
}			else {{
				for (int i = 0; i<numNodes; i++){
					if (isDebug) {{
						{
							// standardOutput<-println("Creating node "+i+" locally")
							{
								Object _arguments[] = { "Creating node "+i+" locally" };
								Message message = new Message( self, standardOutput, "println", _arguments, null, null );
								__messages.add( message );
							}
						}
					}
}					Node nd = ((Node)new Node(this).construct(i, n, ((Main)self)));
					nodes.add(nd);
				}
				for (int i = 0; i<numNodes; i++){
					for (int j = 0; j<n; j++){
						int connectingNodeId = ((int)(i+Math.pow(2, j)))%(int)Math.pow(2, n);
						{
							// standardOutput<-println("Connection from "+i+" to "+connectingNodeId)
							{
								Object _arguments[] = { "Connection from "+i+" to "+connectingNodeId };
								Message message = new Message( self, standardOutput, "println", _arguments, null, null );
								__messages.add( message );
							}
						}
						{
							// (Node)nodes.get(i)<-addConnection((Node)nodes.get(connectingNodeId), connectingNodeId)
							{
								Object _arguments[] = { (Node)nodes.get(connectingNodeId), connectingNodeId };
								Message message = new Message( self, (Node)nodes.get(i), "addConnection", _arguments, null, null );
								__messages.add( message );
							}
						}
					}
				}
			}
}		}
		public void checkForInput() {
			{
				Token token_2_0 = new Token();
				Token token_2_1 = new Token();
				// standardInput<-readLine()
				{
					Object _arguments[] = {  };
					Message message = new Message( self, standardInput, "readLine", _arguments, null, token_2_0 );
					__messages.add( message );
				}
				// handleInput(token)
				{
					Object _arguments[] = { token_2_0 };
					Message message = new Message( self, self, "handleInput", _arguments, token_2_0, token_2_1 );
					__messages.add( message );
				}
				// checkForInput()
				{
					Object _arguments[] = {  };
					Message message = new Message( self, self, "checkForInput", _arguments, token_2_1, null );
					__messages.add( message );
				}
			}
		}
		public void handleInput(String input) {
			String[] tokens = input.split(" ");
			switch (tokens[0]) {
			case "insert": {
				if (tokens.length<4) {{
					{
						// standardError<-println("[error] Usage: insert <fromNode> <key> <value>")
						{
							Object _arguments[] = { "[error] Usage: insert <fromNode> <key> <value>" };
							Message message = new Message( self, standardError, "println", _arguments, null, null );
							__messages.add( message );
						}
					}
break;				}
}				int fromNode = Integer.parseInt(tokens[1]);
				String key = tokens[2];
				String value = tokens[3];
				if (isDebug) {{
					Token h = new Token("h");
					{
						// token h = Hash(key, numNodes)
						{
							Object _arguments[] = { key, numNodes };
							Message message = new Message( self, self, "Hash", _arguments, null, h );
							__messages.add( message );
						}
						// printHashNode(h, true)
						{
							Object _arguments[] = { h, true };
							Message message = new Message( self, self, "printHashNode", _arguments, h, null );
							__messages.add( message );
						}
					}
				}
}				Node nd = (Node)nodes.get(fromNode);
				{
					// nd<-dealWithInsert(Hash(key, numNodes), key, value)
					{
						Object _arguments[] = { Hash(key, numNodes), key, value };
						Message message = new Message( self, nd, "dealWithInsert", _arguments, null, null );
						__messages.add( message );
					}
				}
break;			}
			case "query": {
				if (tokens.length<4) {{
					{
						// standardError<-println("[error] Usage: query <queryID> <fromNode> <key>")
						{
							Object _arguments[] = { "[error] Usage: query <queryID> <fromNode> <key>" };
							Message message = new Message( self, standardError, "println", _arguments, null, null );
							__messages.add( message );
						}
					}
break;				}
}				int queryID = Integer.parseInt(tokens[1]);
				int fromNode = Integer.parseInt(tokens[2]);
				String key = tokens[3];
				if (isDebug) {{
					Token h = new Token("h");
					{
						// token h = Hash(key, numNodes)
						{
							Object _arguments[] = { key, numNodes };
							Message message = new Message( self, self, "Hash", _arguments, null, h );
							__messages.add( message );
						}
						// printHashNode(h, false)
						{
							Object _arguments[] = { h, false };
							Message message = new Message( self, self, "printHashNode", _arguments, h, null );
							__messages.add( message );
						}
					}
				}
}				Node nd = (Node)nodes.get(fromNode);
				{
					// nd<-dealWithQuery(queryID, Hash(key, numNodes), key, fromNode)
					{
						Object _arguments[] = { queryID, Hash(key, numNodes), key, fromNode };
						Message message = new Message( self, nd, "dealWithQuery", _arguments, null, null );
						__messages.add( message );
					}
				}
break;			}
			default: 			{
				// standardError<-println("[error] Command \""+tokens[0]+"\" not recognized.")
				{
					Object _arguments[] = { "[error] Command \""+tokens[0]+"\" not recognized." };
					Message message = new Message( self, standardError, "println", _arguments, null, null );
					__messages.add( message );
				}
			}
			}
		}
		public void println(String str) {
			{
				// standardOutput<-println(str)
				{
					Object _arguments[] = { str };
					Message message = new Message( self, standardOutput, "println", _arguments, null, null );
					__messages.add( message );
				}
			}
		}
		public void printHashNode(int h, boolean isInsert) {
			if (isInsert) {{
				{
					// standardOutput<-println("Hashed node for insert: "+h)
					{
						Object _arguments[] = { "Hashed node for insert: "+h };
						Message message = new Message( self, standardOutput, "println", _arguments, null, null );
						__messages.add( message );
					}
				}
			}
}			else {{
				{
					// standardOutput<-println("Hashed node for query: "+h)
					{
						Object _arguments[] = { "Hashed node for query: "+h };
						Message message = new Message( self, standardOutput, "println", _arguments, null, null );
						__messages.add( message );
					}
				}
			}
}		}
		public void queryResponse(int qID, int startingID, String k, int nodeID, String v) {
			{
				// standardOutput<-println("Request "+qID+" sent to agent "+startingID+": Value for key \""+k+"\" stored in node "+nodeID+": \""+v+"\"")
				{
					Object _arguments[] = { "Request "+qID+" sent to agent "+startingID+": Value for key \""+k+"\" stored in node "+nodeID+": \""+v+"\"" };
					Message message = new Message( self, standardOutput, "println", _arguments, null, null );
					__messages.add( message );
				}
			}
		}
		public int Hash(String key, int numNodes) {
			int sum = 0;
			for (int i = 0; i<key.length(); i++){
				sum += key.charAt(i);
			}
			return sum%numNodes;
		}
	}
}