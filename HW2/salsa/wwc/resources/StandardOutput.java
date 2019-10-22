package wwc.resources;

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
import salsa.language.Token;
import salsa.language.exceptions.*;
import salsa.language.exceptions.CurrentContinuationException;

import salsa.language.UniversalActor;

import salsa.naming.UAN;
import salsa.naming.UAL;
import salsa.naming.MalformedUALException;
import salsa.naming.MalformedUANException;

// End SALSA compiler generated import delcarations.

import salsa.resources.OutputService;

public class StandardOutput extends UniversalActor implements OutputService {
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
		StandardOutput instance = (StandardOutput)new StandardOutput(uan, ual).construct();
		{
			Object[] _arguments = { args };
			instance.send( new Message(instance, instance, "act", _arguments, null, null) );
		}
	}

	public static ActorReference getReferenceByName(UAN uan)	{ return new StandardOutput(false, uan); }
	public static ActorReference getReferenceByName(String uan)	{ return StandardOutput.getReferenceByName(new UAN(uan)); }
	public static ActorReference getReferenceByLocation(UAL ual)	{ return new StandardOutput(false, ual); }

	public static ActorReference getReferenceByLocation(String ual)	{ return StandardOutput.getReferenceByLocation(new UAL(ual)); }
	public StandardOutput(boolean o, UAN __uan)	{ super(__uan); }
	public StandardOutput(boolean o, UAL __ual)	{ super(__ual); }

	public StandardOutput(UAN __uan)	{ this(__uan, null); }
	public StandardOutput(UAL __ual)	{ this(null, __ual); }
	public StandardOutput()		{ this(null, null);  }
	public StandardOutput(UAN __uan, UAL __ual) {
		if (__ual != null && !__ual.getLocation().equals(ServiceFactory.getTheater().getLocation())) {
			createRemotely(__uan, __ual, "wwc.resources.StandardOutput",null);
		} else {
			State state = new State(__uan, __ual);
			state.updateSelf(this);
			ServiceFactory.getNaming().setEntry(state.getUAN(), state.getUAL(), state);
			if (getUAN() != null) ServiceFactory.getNaming().update(state.getUAN(), state.getUAL());
		}
	}

	public UniversalActor construct () {
		Object[] __arguments = {  };
		this.send( new Message(this, this, "construct", __arguments, null, null) );
		return this;
	}

	public class State extends UniversalActor.State  implements salsa.resources.EnvironmentalServiceState {
		public StandardOutput self;
		public void updateSelf(ActorReference actorReference) {
			self = (StandardOutput)actorReference;
			self.setUAN(getUAN());
			self.setUAL(getUAL());
		}

		public State() {
			this(null, null);
		}

		public State(UAN __uan, UAL __ual) {
			super(__uan, __ual);
			addClassName( "wwc.resources.StandardOutput$State" );
			addMethodsForClasses();
		}

		public void process(Message message) {
			Method[] matches = getMatches(message.getMethodName());
			Object returnValue = null;
			Exception exception = null;

			if (matches != null) {
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

		public void construct(){
		}
		public void print(Object p) {
			System.out.print(p);
		}
		public void print(boolean p) {
			System.out.print(p);
		}
		public void print(char p) {
			System.out.print(p);
		}
		public void print(byte p) {
			System.out.print(p);
		}
		public void print(short p) {
			System.out.print(p);
		}
		public void print(int p) {
			System.out.print(p);
		}
		public void print(long p) {
			System.out.print(p);
		}
		public void print(float p) {
			System.out.print(p);
		}
		public void print(double p) {
			System.out.print(p);
		}
		public void println(Object p) {
			System.out.println(p);
		}
		public void println(boolean p) {
			System.out.println(p);
		}
		public void println(char p) {
			System.out.println(p);
		}
		public void println(byte p) {
			System.out.println(p);
		}
		public void println(short p) {
			System.out.println(p);
		}
		public void println(int p) {
			System.out.println(p);
		}
		public void println(long p) {
			System.out.println(p);
		}
		public void println(float p) {
			System.out.println(p);
		}
		public void println(double p) {
			System.out.println(p);
		}
		public void println() {
			System.out.println();
		}
	}
}