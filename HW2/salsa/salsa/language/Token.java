/*
 *	Modified Extensively for SALSA 0.6.0 new token semantics.
 *
 *	Author: Travis Desell
 *
 *	Generates the input and output tokens as generated by the salsa
 *	compiler's message sending code.
 *
 *      Modified by Wei-Jen Wang to remove join director and to support actor GC
 */

package salsa.language;

import java.util.Vector;
import gc.WeakReference;
import salsa.naming.UAN;
import salsa.naming.UAL;

public class Token implements java.io.Serializable {
	private String owner;
        public void setOwner(String owner) {
          if (this.owner==null || this.owner.length()==0) {
            this.owner = owner;
          }
        }
        public String getOwner() {return owner;}
	/**
	 * If a token was specified a value at creation
	 * (through code like:  token x = 5), this method
	 * is used by the method to retrieve that value instead
	 * of waiting for a token.
	 */
	private Object	value = null;
	public	Object	getValue() 		{ return value; }

	/**
	 * The compiler specifies that this token is part of
	 * a join continuation by setJoinDirector.  Message then
	 * uses isJoinDirector to determine if this token is
	 * part of a join continuation or not.
	 */
        private transient boolean isUnderJoinBlock = false;

	private transient boolean	isJoinDirector = false;
	public	boolean	isJoinDirector()	{ return isJoinDirector; }
	public	void	setJoinDirector() {
		if (isJoinDirector) {
			System.err.println("Token Creation Error:");
			System.err.println("\tAttempted to nest join blocks, currently this is not supported.");
			System.err.println("\tThis will cause errors with the current join block and it's return value.");
			System.err.println("\tWith token's owner: " + owner);
		}
                JoinWrap joinWrap=new JoinWrap();
                isUnderJoinBlock=true;
		isJoinDirector = true;
	}

        private boolean isJoinInput = false;
        public boolean isJoinInput() {return isJoinInput;}
        public void setisJoinInput(boolean val) {isJoinInput=val;}

	/**
	 * If this token is used as a token for a join director
	 * it needs to keep track of how many inputs that join
	 * director has.  This method is used by message to specify
	 * how many there are.
	 */
	//private int joinInputs = 0;
	//public int getJoinPosition() {
        //  if (this.isUnderJoinBlock) {
        //    joinInputs++;
        //  }
        //  return joinInputs-1;
	//}

	/**
	 * These methods create a join director and return a reference
	 * to that join director so tokens can be resolved to it.  The
	 * join director is passed a reference to this token, so it
	 * knows where to send its return value to.
	 */

	//private ActorReference joinDirector = null;
	public void createJoinDirector() {
                isUnderJoinBlock=false;
                /*if (joinInputs>0) {
                  //there are tokens to be joined
                  joinDirector = (JoinDirector)new JoinDirector().construct(joinInputs,
                      targetActors, targetMessages, targetPositions);
                } else {
                  //send the token immediately
                  for (int i = 0; i<targetActors.size(); i++){
                          ActorReference target = (ActorReference)targetActors.get(i);
                          String messageId = (String)targetMessages.get(i);
                          Integer position = (Integer)targetPositions.get(i);
                          Object[] arguments = new Object[3];
                          arguments[0] = messageId;
                          arguments[1] = position;
                          Object[] args={};
                          if (position.intValue()==-3) arguments[2] = null;
                          else arguments[2] = args;
                          target.send(new Message(target, target, "resolveToken", arguments, null, null));
                  }
                }*/
	}
	//public ActorReference getJoinDirector() {
	//	return joinDirector;
	//}


        /**
         * for synchronization token
         * a message cannot be processed unless sourceActors = null
         * sourcePositions is use to identify the position of return values of a join token
         */

        public class JoinWrap implements java.io.Serializable{
          private Object[] joinArgs = null;
          private int requiredTokens=0;
          private int joinPosition=-3;
          private transient Vector joinInputTokens=null;
          private transient Vector joinMessageIds=null;
          private transient Token joinJoinToken=null;

          public void setJoinJoinToken(Token data) {joinJoinToken=data;}
          public Token getJoinJoinToken() {return joinJoinToken;}

          public void setJoinPosition(int pos) {joinPosition=pos;}
          public int getJoinPosition(){return joinPosition;}

          public JoinWrap() {}

          public void addJoinMessageId(String messageId) {
            if (joinMessageIds==null) {joinMessageIds=new Vector();}
            joinMessageIds.addElement(messageId);
          }

          public Object[] getJoinArgs() {return joinArgs;}

          public void resolveJoinToken(Object value, int position) {
            if (joinArgs==null) {
              joinArgs=new Object[requiredTokens];
            }
            if (position>=0) {
              joinArgs[position] = value;
              if (value!=null && value instanceof UniversalActor) {
                ((UniversalActor)value).toInMessageState();
              }
            }
            requiredTokens--;
          }

          public void addWaitingMsg() {
            requiredTokens++;
          }

          public boolean isEmpty() {
            return requiredTokens==0;
          }

          public void addJoinInputTokens(Token joinInput) {
            if (joinInputTokens==null) {joinInputTokens=new Vector();}
            joinInputTokens.addElement(joinInput);
            requiredTokens++;
          }

          public void setJoinInputsTarget(ActorReference targetRef) {
            if (joinInputTokens==null) {return;}
            for (int i=0;i<joinInputTokens.size();i++) {
              Token input = (Token) joinInputTokens.get(i);
              for (int j=0;j<joinMessageIds.size();j++) {
                String messageId= (String) joinMessageIds.get(j);
                input.addTarget(targetRef, messageId, i);
              }
            }
          }
        }

        private JoinWrap joinData=null;
        private Vector sourceActors = null;

        public void setJoinJoinToken(Token data) {joinData.setJoinJoinToken(data);}
          public Token getJoinJoinToken() {return joinData.getJoinJoinToken();}

        public void addJoinMessageId(String messageId) {
          if (joinData==null) {joinData=new JoinWrap();}
          joinData.addJoinMessageId(messageId);
        }

        public void addJoinInputTokens(Token joinInput) {
          if (joinData==null) {joinData=new JoinWrap();}
          joinData.addJoinInputTokens(joinInput);
        }

        public void setJoinInputsTarget() {
          WeakReference targetRef;
          if (owner==null || owner.equals("")) {return;}
          if (joinData==null) {joinData=new JoinWrap();}
          if (owner.charAt(0)=='u' || this.owner.charAt(0)=='U') {
            targetRef=new WeakReference(new UAN(owner),null);
          } else {
            targetRef=new WeakReference(null,new UAL(owner));
          }
          joinData.setJoinInputsTarget(targetRef);
        }

        public void setJoinData(JoinWrap data) {joinData=data;}
        public JoinWrap getJoinData() {return joinData;}

        public boolean isResolved() {
          return sourceActors==null || sourceActors.size()==0;
        }

        public boolean isJoinResolved() {
          return joinData==null || joinData.isEmpty();
        }

        public void addSync(String fromRef) {
          if (sourceActors==null) {sourceActors=new Vector();}
          sourceActors.add(fromRef);
//System.out.println("+++"+fromRef);
        }

        public void addSync(Vector fromRefs) {
          if (fromRefs==null || fromRefs.size()==0) {return;}
          if (sourceActors==null) {sourceActors=new Vector();}
          sourceActors.addAll(fromRefs);
//System.out.println("+++:"+fromRefs);
        }

        public Vector getSync() {return sourceActors;}

        public void resolveSync(String fromRef) {
             for (int i=0;i<sourceActors.size();i++) {
               if (((String)sourceActors.get(i)).equals(fromRef)) {
                 sourceActors.remove(i);
//System.out.println("---"+fromRef);
                 return;
               }
             }
 System.err.println("Continuation Token Resolution Error:"+fromRef+" does not exist!");
 System.err.println("The actor is waiting for: ");
 for (int i=0;i<sourceActors.size();i++) {
   System.err.println("\t"+ (String)sourceActors.get(i));
 }
        }

        public void receiveJoinToken(Object value, int position) {
          joinData.resolveJoinToken(value,position);
        }

        public void setJoinPosition(int pos) {joinData.joinPosition=pos;}
        public int getJoinPosition(){return joinData.getJoinPosition();}
        public void clearJoinData() {joinData=null;}

	/**
	 * These vectors are used to store the information about
	 * where to send tokens, and how they are resolved.
	 */
	private Vector targetActors = new Vector();
	private Vector targetMessages = new Vector();
	private Vector targetPositions = new Vector();

        public Vector getTargetActors() {return targetActors;}

	/**
	 * This method is used by message to add an additional target
	 * to this token.
	 */
	public void addTarget(ActorReference actorReference, String messageId, int position) {
		targetActors.add(actorReference);
		targetMessages.add(messageId);
		targetPositions.add( new Integer(position) );
	}

	/**
	 * When a message has been processed and a value has been
	 * returned, this sends that value to each actor which has
	 * a message which it is being used in.
	 */
	public void resolve(ActorReference source, Object value) {
		for (int i = 0; i < targetActors.size(); i++) {
			ActorReference target = (ActorReference)targetActors.get(i);
			String messageId = (String)targetMessages.get(i);
			Integer position = (Integer)targetPositions.get(i);

			/**
			 * If the value is a large object, it isn't required to be sent
			 * if this token is being used for synchronization.  We can send
			 * null instead.
			 */
			Object[] arguments = new Object[4];
			arguments[0] = messageId;
			arguments[1] = position;
			if (position.intValue() == -3) arguments[2] = null;
			else arguments[2] = value;
                        arguments[3] = owner;
			target.send( new Message(source, target, "resolveToken", arguments, null, null,false));
		}
	}

        public void resolveJoin(ActorReference source, Object value) {
                for (int i = 0; i < targetActors.size(); i++) {
                        ActorReference target = (ActorReference)targetActors.get(i);
                        String messageId = (String)targetMessages.get(i);
                        Integer position = (Integer)targetPositions.get(i);

                        /**
                         * If the value is a large object, it isn't required to be sent
                         * if this token is being used for synchronization.  We can send
                         * null instead.
                         */
                        Object[] arguments = new Object[4];
                        arguments[0] = messageId;
                        arguments[1] = position;
                        if (position.intValue() == -3) arguments[2] = null;
                        else arguments[2] = value;
                        arguments[3] = owner;
                        target.send( new Message(source, target, "resolveJoinToken", arguments, null, null,false));
                }
        }

	/**
	 *	These are the constructors for Token.  They specify the
	 *	tokens name, as well as retrieve a unique ID for this
	 * 	token.  If the token has a specified value it is set.
	 */
	public Token() {
		owner = "";
	}
        public Token(String name) {
                this();
                //this.name=name;
        }

	public Token(String name,String ownerRef) {
		this();
                //this.name=name;
		this.owner = ownerRef;
	}

	public Token(String name,String ownerRef, Object value) {
		this(name,ownerRef);
		this.value = value;
	}
}
