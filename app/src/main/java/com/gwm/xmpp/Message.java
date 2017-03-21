package com.gwm.xmpp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.util.StringUtils;
/**
 * XMPP之Message节点

XMPP,
0 Comments, March 22, 2013, 9:14 p.m.
版权所有，转载请注明出处：http://guangboo.org/2013/03/22/xmpp-message-stanza

XML节是XMPP通讯的基本单元，类似于其他网络协议的数据包或消息，XML节包含以下概念：

节点名，包括message, presence和iq，每种节都要各种的作用，客户端也会区别处理；

属性，XML节都有各自的属性，并且这些属性都有重要的意义；

子节点，可以定义节的承载的数据等，这些承载可能会展示给用户，也可能是承载命名空间下定义的其他行为。

下面针对XMPP中包含的三种节点，message, presence, iq，进行介绍。

Message节

XMPP的<message/>节点是通讯过程中基本的“推”方式，它是一种“发出不管”的机制，来快速进行消息的获取。消息通常用在IM,群聊，提醒和通知，及其他应用。

Message节点有一个type属性，该属性有以下5个可能值，分别起到不同的作用：

normal
normal类型的消息就像邮件消息一样，因为这样的消息都是独立的，可能有反馈，也可能没有；

chat
chat类型的消息用于双方实时回话的数据交换，如朋友之间的即时消息聊天；

groupchat
groupchat类型的消息用于多用户聊天，即群聊，类似于IRC。

headline
headline类型的消息用于发送提醒和通知，对是否有反馈没有要求。

error
当双方前面发送的消息处理过程中有错误发生，该类型的节点就用反馈错误信息。

除type属性外，message节还包含了from，to属性，其中都是JID，分别表示消息的源和目的地址。message还可以承载一些子节点，XMPP核心规范中定义以下基本的承载，如<body/>和<subject/>，都是用于聊天消息，如下：
<message from="madhatter@wonderland.lit/foo"
    to="alice@wonderland.lit"
    type="chat">
    <body>Who are you?</body>
    <subject>Query</subject>
</message>
当然message节点，包括之后介绍的其他节点，都可以承载XMPP核心协议中没有定义的子节点，用于节点的扩展。
 * @author asus1
 *
 */
public class Message extends Packet {
    private Type type = Type.normal;
    private String thread = null;
    private String language;

    private final Set<Subject> subjects = new HashSet<Subject>();
    private final Set<Body> bodies = new HashSet<Body>();

    /**
     * Creates a new, "normal" message.
     */
    public Message() {
    }

    /**
     * Creates a new "normal" message to the specified recipient.
     *
     * @param to the recipient of the message.
     */
    public Message(String to) {
        setTo(to);
    }

    /**
     * Creates a new message of the specified type to a recipient.
     *
     * @param to the user to send the message to.
     * @param type the message type.
     */
    public Message(String to, Type type) {
        setTo(to);
        this.type = type;
    }

    /**
     * Returns the type of the message. If no type has been set this method will return {@link
     * org.jivesoftware.smack.packet.Message.Type#normal}.
     *
     * @return the type of the message.
     */
    public Type getType() {
        return type;
    }

    /**
     * Sets the type of the message.
     *
     * @param type the type of the message.
     * @throws IllegalArgumentException if null is passed in as the type
     */
    public void setType(Type type) {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null.");
        }
        this.type = type;
    }

    /**
     * Returns the default subject of the message, or null if the subject has not been set.
     * The subject is a short description of message contents.
     * <p>
     * The default subject of a message is the subject that corresponds to the message's language.
     * (see {@link #getLanguage()}) or if no language is set to the applications default
     * language (see {@link Packet#getDefaultLanguage()}).
     *
     * @return the subject of the message.
     */
    public String getSubject() {
        return getSubject(null);
    }
    
    /**
     * Returns the subject corresponding to the language. If the language is null, the method result
     * will be the same as {@link #getSubject()}. Null will be returned if the language does not have
     * a corresponding subject.
     *
     * @param language the language of the subject to return.
     * @return the subject related to the passed in language.
     */
    public String getSubject(String language) {
        Subject subject = getMessageSubject(language);
        return subject == null ? null : subject.subject;
    }
    
    private Subject getMessageSubject(String language) {
        language = determineLanguage(language);
        for (Subject subject : subjects) {
            if (language.equals(subject.language)) {
                return subject;
            }
        }
        return null;
    }

    /**
     * Returns a set of all subjects in this Message, including the default message subject accessible
     * from {@link #getSubject()}.
     *
     * @return a collection of all subjects in this message.
     */
    public Collection<Subject> getSubjects() {
        return Collections.unmodifiableCollection(subjects);
    }

    /**
     * Sets the subject of the message. The subject is a short description of
     * message contents.
     *
     * @param subject the subject of the message.
     */
    public void setSubject(String subject) {
        if (subject == null) {
            removeSubject(""); // use empty string because #removeSubject(null) is ambiguous 
            return;
        }
        addSubject(null, subject);
    }

    /**
     * Adds a subject with a corresponding language.
     *
     * @param language the language of the subject being added.
     * @param subject the subject being added to the message.
     * @return the new {@link org.jivesoftware.smack.packet.Message.Subject}
     * @throws NullPointerException if the subject is null, a null pointer exception is thrown
     */
    public Subject addSubject(String language, String subject) {
        language = determineLanguage(language);
        Subject messageSubject = new Subject(language, subject);
        subjects.add(messageSubject);
        return messageSubject;
    }

    /**
     * Removes the subject with the given language from the message.
     *
     * @param language the language of the subject which is to be removed
     * @return true if a subject was removed and false if it was not.
     */
    public boolean removeSubject(String language) {
        language = determineLanguage(language);
        for (Subject subject : subjects) {
            if (language.equals(subject.language)) {
                return subjects.remove(subject);
            }
        }
        return false;
    }

    /**
     * Removes the subject from the message and returns true if the subject was removed.
     *
     * @param subject the subject being removed from the message.
     * @return true if the subject was successfully removed and false if it was not.
     */
    public boolean removeSubject(Subject subject) {
        return subjects.remove(subject);
    }

    /**
     * Returns all the languages being used for the subjects, not including the default subject.
     *
     * @return the languages being used for the subjects.
     */
    public Collection<String> getSubjectLanguages() {
        Subject defaultSubject = getMessageSubject(null);
        List<String> languages = new ArrayList<String>();
        for (Subject subject : subjects) {
            if (!subject.equals(defaultSubject)) {
                languages.add(subject.language);
            }
        }
        return Collections.unmodifiableCollection(languages);
    }

    /**
     * Returns the default body of the message, or null if the body has not been set. The body
     * is the main message contents.
     * <p>
     * The default body of a message is the body that corresponds to the message's language.
     * (see {@link #getLanguage()}) or if no language is set to the applications default
     * language (see {@link Packet#getDefaultLanguage()}).
     *
     * @return the body of the message.
     */
    public String getBody() {
        return getBody(null);
    }

    /**
     * Returns the body corresponding to the language. If the language is null, the method result
     * will be the same as {@link #getBody()}. Null will be returned if the language does not have
     * a corresponding body.
     *
     * @param language the language of the body to return.
     * @return the body related to the passed in language.
     * @since 3.0.2
     */
    public String getBody(String language) {
        Body body = getMessageBody(language);
        return body == null ? null : body.message;
    }
    
    private Body getMessageBody(String language) {
        language = determineLanguage(language);
        for (Body body : bodies) {
            if (language.equals(body.language)) {
                return body;
            }
        }
        return null;
    }

    /**
     * Returns a set of all bodies in this Message, including the default message body accessible
     * from {@link #getBody()}.
     *
     * @return a collection of all bodies in this Message.
     * @since 3.0.2
     */
    public Collection<Body> getBodies() {
        return Collections.unmodifiableCollection(bodies);
    }

    /**
     * Sets the body of the message. The body is the main message contents.
     *
     * @param body the body of the message.
     */
    public void setBody(String body) {
        if (body == null) {
            removeBody(""); // use empty string because #removeBody(null) is ambiguous
            return;
        }
        addBody(null, body);
    }

    /**
     * Adds a body with a corresponding language.
     *
     * @param language the language of the body being added.
     * @param body the body being added to the message.
     * @return the new {@link org.jivesoftware.smack.packet.Message.Body}
     * @throws NullPointerException if the body is null, a null pointer exception is thrown
     * @since 3.0.2
     */
    public Body addBody(String language, String body) {
        language = determineLanguage(language);
        Body messageBody = new Body(language, body);
        bodies.add(messageBody);
        return messageBody;
    }

    /**
     * Removes the body with the given language from the message.
     *
     * @param language the language of the body which is to be removed
     * @return true if a body was removed and false if it was not.
     */
    public boolean removeBody(String language) {
        language = determineLanguage(language);
        for (Body body : bodies) {
            if (language.equals(body.language)) {
                return bodies.remove(body);
            }
        }
        return false;
    }

    /**
     * Removes the body from the message and returns true if the body was removed.
     *
     * @param body the body being removed from the message.
     * @return true if the body was successfully removed and false if it was not.
     * @since 3.0.2
     */
    public boolean removeBody(Body body) {
        return bodies.remove(body);
    }

    /**
     * Returns all the languages being used for the bodies, not including the default body.
     *
     * @return the languages being used for the bodies.
     * @since 3.0.2
     */
    public Collection<String> getBodyLanguages() {
        Body defaultBody = getMessageBody(null);
        List<String> languages = new ArrayList<String>();
        for (Body body : bodies) {
            if (!body.equals(defaultBody)) {
                languages.add(body.language);
            }
        }
        return Collections.unmodifiableCollection(languages);
    }

    /**
     * Returns the thread id of the message, which is a unique identifier for a sequence
     * of "chat" messages. If no thread id is set, <tt>null</tt> will be returned.
     *
     * @return the thread id of the message, or <tt>null</tt> if it doesn't exist.
     */
    public String getThread() {
        return thread;
    }

    /**
     * Sets the thread id of the message, which is a unique identifier for a sequence
     * of "chat" messages.
     *
     * @param thread the thread id of the message.
     */
    public void setThread(String thread) {
        this.thread = thread;
    }

    /**
     * Returns the xml:lang of this Message.
     *
     * @return the xml:lang of this Message.
     * @since 3.0.2
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the xml:lang of this Message.
     *
     * @param language the xml:lang of this Message.
     * @since 3.0.2
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    private String determineLanguage(String language) {
        
        // empty string is passed by #setSubject() and #setBody() and is the same as null
        language = "".equals(language) ? null : language;

        // if given language is null check if message language is set
        if (language == null && this.language != null) {
            return this.language;
        }
        else if (language == null) {
            return getDefaultLanguage();
        }
        else {
            return language;
        }
        
    }

    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<message");
        if (getXmlns() != null) {
            buf.append(" xmlns=\"").append(getXmlns()).append("\"");
        }
        if (language != null) {
            buf.append(" xml:lang=\"").append(getLanguage()).append("\"");
        }
        if (getPacketID() != null) {
            buf.append(" id=\"").append(getPacketID()).append("\"");
        }
        if (getTo() != null) {
            buf.append(" to=\"").append(StringUtils.escapeForXML(getTo())).append("\"");
        }
        if (getFrom() != null) {
            buf.append(" from=\"").append(StringUtils.escapeForXML(getFrom())).append("\"");
        }
        if (type != Type.normal) {
            buf.append(" type=\"").append(type).append("\"");
        }
        buf.append(">");
        // Add the subject in the default language
        Subject defaultSubject = getMessageSubject(null);
        if(defaultSubject!=null){
        	buf.append("<subject>").append(StringUtils.escapeForXML(defaultSubject.getSubject()));
        	buf.append("</subject>");
        }
        //Add subjects in other languages
        for ( Subject s : getSubjects()){
        	buf.append("<subject xml:lang=\""+s.getLanguage()+"\">");
        	buf.append(StringUtils.escapeForXML(s.getSubject()));
        	buf.append("</subject>");
        }
        // Add the body in the default language
        Body defaultBody = getMessageBody(null);
        if (defaultBody != null) {
            buf.append("<body>").append(StringUtils.escapeForXML(defaultBody.message)).append("</body>");
        }
        // Add the bodies in other languages
        for (Body body : getBodies()) {
            // Skip the default language
            if(body.equals(defaultBody))
                continue;
            buf.append("<body xml:lang=\"").append(body.getLanguage()).append("\">");
            buf.append(StringUtils.escapeForXML(body.getMessage()));
            buf.append("</body>");
        }
        if (thread != null) {
            buf.append("<thread>").append(thread).append("</thread>");
        }
        // Append the error subpacket if the message type is an error.
        if (type == Type.error) {
            XMPPError error = getError();
            if (error != null) {
                buf.append(error.toXML());
            }
        }
        // Add packet extensions, if any are defined.
        buf.append(getExtensionsXML());
        buf.append("</message>");
        return buf.toString();
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if(!super.equals(message)) { return false; }
        if (bodies.size() != message.bodies.size() || !bodies.containsAll(message.bodies)) {
            return false;
        }
        if (language != null ? !language.equals(message.language) : message.language != null) {
            return false;
        }
        if (subjects.size() != message.subjects.size() || !subjects.containsAll(message.subjects)) {
            return false;
        }
        if (thread != null ? !thread.equals(message.thread) : message.thread != null) {
            return false;
        }
        return type == message.type;

    }

    public int hashCode() {
        int result;
        result = (type != null ? type.hashCode() : 0);
        result = 31 * result + subjects.hashCode();
        result = 31 * result + (thread != null ? thread.hashCode() : 0);
        result = 31 * result + (language != null ? language.hashCode() : 0);
        result = 31 * result + bodies.hashCode();
        return result;
    }

    /**
     * Represents a message subject, its language and the content of the subject.
     */
    public static class Subject {

        private String subject;
        private String language;

        private Subject(String language, String subject) {
            if (language == null) {
                throw new NullPointerException("Language cannot be null.");
            }
            if (subject == null) {
                throw new NullPointerException("Subject cannot be null.");
            }
            this.language = language;
            this.subject = subject;
        }

        /**
         * Returns the language of this message subject.
         *
         * @return the language of this message subject.
         */
        public String getLanguage() {
            return language;
        }

        /**
         * Returns the subject content.
         *
         * @return the content of the subject.
         */
        public String getSubject() {
            return subject;
        }


        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) { return false; }

            Subject otherSubject = (Subject) o;

            if (!language.equals(otherSubject.language)) {
                return false;
            }
            return subject.equals(otherSubject.subject);

        }

        public int hashCode() {
            int result;
            result = subject.hashCode();
            result = 31 * result + language.hashCode();
            return result;
        }
    }

    /**
     * Represents a message body, its language and the content of the message.
     */
    public static class Body {

        private String message;
        private String language;

        private Body(String language, String message) {
            if (language == null) {
                throw new NullPointerException("Language cannot be null.");
            }
            if (message == null) {
                throw new NullPointerException("Message cannot be null.");
            }
            this.language = language;
            this.message = message;
        }

        /**
         * Returns the language of this message body.
         *
         * @return the language of this message body.
         */
        public String getLanguage() {
            return language;
        }

        /**
         * Returns the message content.
         *
         * @return the content of the message.
         */
        public String getMessage() {
            return message;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) { return false; }

            Body otherBody = (Body) o;

            if (language != null ? !language.equals(otherBody.language) : otherBody.language != null) {
                return false;
            }
            return message.equals(otherBody.message);

        }

        public int hashCode() {
            int result;
            result = message.hashCode();
            result = 31 * result + (language != null ? language.hashCode() : 0);
            return result;
        }
        
    }

    /**
     * Represents the type of a message.
     */
    public enum Type {

        /**
         * (Default) a normal text message used in email like interface.
         */
        normal,

        /**
         * Typically short text message used in line-by-line chat interfaces.
         */
        chat,

        /**
         * Chat message sent to a groupchat server for group chats.
         */
        groupchat,

        /**
         * Text message to be displayed in scrolling marquee displays.
         */
        headline,

        /**
         * indicates a messaging error.
         */
        error;

        public static Type fromString(String name) {
            try {
                return Type.valueOf(name);
            }
            catch (Exception e) {
                return normal;
            }
        }

    }

}
