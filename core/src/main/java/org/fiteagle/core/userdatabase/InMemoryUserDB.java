package org.fiteagle.core.userdatabase;

import java.util.HashMap;

public class InMemoryUserDB implements UserPersistable {

  private HashMap<String, User> users;

	public InMemoryUserDB(){
		users = new HashMap<String, User>();
	}

	public int getNumberOfUsers(){
		return users.size();
	}

	@Override
	public void add(User u) throws DuplicateUsernameException, NotEnoughAttributesException, InValidAttributeException, DuplicatePublicKeyException {
		if(users.get(u.getUsername()) != null)
			throw new DuplicateUsernameException();
	  u.checkAttributes();
		users.put(u.getUsername(), u);
	}

	@Override
	public void delete(String username){
		users.remove(username);			
	}

	@Override
	public void delete(User u){
		delete(u.getUsername());
	}

	@Override
	public void update(User u) throws RecordNotFoundException, NotEnoughAttributesException, InValidAttributeException, DuplicatePublicKeyException {
		if(users.get(u.getUsername()) == null)
			throw new RecordNotFoundException();
	  User newUser = get(u.getUsername());
	  newUser.mergeWithUser(u);		 
	  users.put(u.getUsername(), newUser);    
	}

	@Override
	public User get(String username) throws RecordNotFoundException {
		if(users.get(username) == null)
			throw new RecordNotFoundException();
		return users.get(username);		
	}

	@Override
	public User get(User u) throws RecordNotFoundException {
		return get(u.getUsername());		
	}

	@Override
	public void addKey(String username, UserPublicKey key) throws RecordNotFoundException, InValidAttributeException, DuplicatePublicKeyException {
	  User u = get(username);		
		u.addPublicKey(key);
	}
	
	@Override
	public void deleteKey(String username, String description) throws RecordNotFoundException, DatabaseException, InValidAttributeException {
	  if(description == null || description.length() == 0)
      throw new InValidAttributeException("no valid description");    
	  User u = get(username);    
    u.deletePublicKey(description);
	}	
}