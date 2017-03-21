package com.gwm.xmpp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.packet.IQ;

public class Registration extends IQ {
    private String instructions = null;
    private Map<String, String> attributes = new HashMap<String,String>();
    private List<String> requiredFields = new ArrayList<String>();
    private boolean registered = false;
    private boolean remove = false;

    /**
     * Returns the registration instructions, or <tt>null</tt> if no instructions
     * have been set. If present, instructions should be displayed to the end-user
     * that will complete the registration process.
     *
     * @return the registration instructions, or <tt>null</tt> if there are none.
     */
    public String getInstructions() {
        return instructions;
    }

    /**
     * Sets the registration instructions.
     *
     * @param instructions the registration instructions.
     */
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    /**
     * Returns the map of String key/value pairs of account attributes.
     *
     * @return the account attributes.
     */
    public Map<String, String> getAttributes() {
        return attributes;
    }
    
    public void addRequiredField(String field){
    	requiredFields.add(field);
    }
    
    public List<String> getRequiredFields(){
    	return requiredFields;
    }
    
    public void addAttribute(String key, String value){
    	attributes.put(key, value);
    }
    
    public void setRegistered(boolean registered){
    	this.registered = registered;
    }
    
    public boolean isRegistered(){
    	return this.registered;
    }
    
    public String getField(String key){
    	return attributes.get(key);
    }
    
    public List<String> getFieldNames(){
    	return new ArrayList<String>(attributes.keySet());
    }
    
    public void setUsername(String username){
    	attributes.put("username", username);
    }
    
    public void setPassword(String password){
    	attributes.put("password", password);
    }
    
    public void setRemove(boolean remove){
    	this.remove = remove;
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\"jabber:iq:register\">");
        if (instructions != null && !remove) {
            buf.append("<instructions>").append(instructions).append("</instructions>");
        }
        if (attributes != null && attributes.size() > 0 && !remove) {
            for (String name : attributes.keySet()) {
                String value = attributes.get(name);
                buf.append("<").append(name).append(">");
                buf.append(value);
                buf.append("</").append(name).append(">");
            }
        }
        else if(remove){
        	buf.append("</remove>");
        }
        // Add packet extensions, if any are defined.
        buf.append(getExtensionsXML());
        buf.append("</query>");
        return buf.toString();
    }
}
