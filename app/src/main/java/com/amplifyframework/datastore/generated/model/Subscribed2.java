package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Subscribed2 type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Subscribed2s", authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class Subscribed2 implements Model {
  public static final QueryField ID = field("Subscribed2", "id");
  public static final QueryField USER_ID = field("Subscribed2", "userId");
  public static final QueryField SUBSCRIBED_M_PS = field("Subscribed2", "subscribedMPs");
  public static final QueryField SUBSCRIBED_BILLS = field("Subscribed2", "subscribedBills");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String userId;
  private final @ModelField(targetType="String") List<String> subscribedMPs;
  private final @ModelField(targetType="String") List<String> subscribedBills;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public String getUserId() {
      return userId;
  }
  
  public List<String> getSubscribedMPs() {
      return subscribedMPs;
  }
  
  public List<String> getSubscribedBills() {
      return subscribedBills;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private Subscribed2(String id, String userId, List<String> subscribedMPs, List<String> subscribedBills) {
    this.id = id;
    this.userId = userId;
    this.subscribedMPs = subscribedMPs;
    this.subscribedBills = subscribedBills;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Subscribed2 subscribed2 = (Subscribed2) obj;
      return ObjectsCompat.equals(getId(), subscribed2.getId()) &&
              ObjectsCompat.equals(getUserId(), subscribed2.getUserId()) &&
              ObjectsCompat.equals(getSubscribedMPs(), subscribed2.getSubscribedMPs()) &&
              ObjectsCompat.equals(getSubscribedBills(), subscribed2.getSubscribedBills()) &&
              ObjectsCompat.equals(getCreatedAt(), subscribed2.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), subscribed2.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserId())
      .append(getSubscribedMPs())
      .append(getSubscribedBills())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Subscribed2 {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("userId=" + String.valueOf(getUserId()) + ", ")
      .append("subscribedMPs=" + String.valueOf(getSubscribedMPs()) + ", ")
      .append("subscribedBills=" + String.valueOf(getSubscribedBills()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static UserIdStep builder() {
      return new Builder();
  }
  
  /** 
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static Subscribed2 justId(String id) {
    return new Subscribed2(
      id,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      userId,
      subscribedMPs,
      subscribedBills);
  }
  public interface UserIdStep {
    BuildStep userId(String userId);
  }
  

  public interface BuildStep {
    Subscribed2 build();
    BuildStep id(String id);
    BuildStep subscribedMPs(List<String> subscribedMPs);
    BuildStep subscribedBills(List<String> subscribedBills);
  }
  

  public static class Builder implements UserIdStep, BuildStep {
    private String id;
    private String userId;
    private List<String> subscribedMPs;
    private List<String> subscribedBills;
    @Override
     public Subscribed2 build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Subscribed2(
          id,
          userId,
          subscribedMPs,
          subscribedBills);
    }
    
    @Override
     public BuildStep userId(String userId) {
        Objects.requireNonNull(userId);
        this.userId = userId;
        return this;
    }
    
    @Override
     public BuildStep subscribedMPs(List<String> subscribedMPs) {
        this.subscribedMPs = subscribedMPs;
        return this;
    }
    
    @Override
     public BuildStep subscribedBills(List<String> subscribedBills) {
        this.subscribedBills = subscribedBills;
        return this;
    }
    
    /** 
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String userId, List<String> subscribedMPs, List<String> subscribedBills) {
      super.id(id);
      super.userId(userId)
        .subscribedMPs(subscribedMPs)
        .subscribedBills(subscribedBills);
    }
    
    @Override
     public CopyOfBuilder userId(String userId) {
      return (CopyOfBuilder) super.userId(userId);
    }
    
    @Override
     public CopyOfBuilder subscribedMPs(List<String> subscribedMPs) {
      return (CopyOfBuilder) super.subscribedMPs(subscribedMPs);
    }
    
    @Override
     public CopyOfBuilder subscribedBills(List<String> subscribedBills) {
      return (CopyOfBuilder) super.subscribedBills(subscribedBills);
    }
  }
  
}
