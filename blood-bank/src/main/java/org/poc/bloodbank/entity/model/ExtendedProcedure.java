package org.poc.bloodbank.entity.model;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.r4.model.*;

import java.util.List;

@ResourceDef(name = "ExtendedProcedure", profile = "http://hl7.org/fhir/Profile/ExtendedProcedure")
public class ExtendedProcedure extends Procedure {
    private static final long serialVersionUID = 1L;

    @JsonBackReference
    @Child(name = "businessStatus", type = {
            StringType.class }, order = 27, min = 0, max = 1, modifier = false, summary = true)
    @Description(shortDefinition = "", formalDefinition = "")
    private StringType businessStatus;

    @JsonBackReference
    @Child(name = "anesthesiaType", type = {
            Coding.class }, order = 28, min = 0, max = 1, modifier = false, summary = true)
    @Description(shortDefinition = "", formalDefinition = "")
    private Coding anesthesiaType;

    @Child(name = "laterality", type = {
            Coding.class }, order = 29, min = 0, max = 1, modifier = false, summary = true)
    @Description(shortDefinition = "", formalDefinition = "")
    private Coding laterality;

    @Child(name = "preparation", type = {
            Coding.class }, order = 30, min = 0, max = 1, modifier = false, summary = true)
    @Description(shortDefinition = "", formalDefinition = "")
    private StringType preparation;

    @Child(name = "position", type = {
            Coding.class }, order = 31, min = 0, max = 1, modifier = false, summary = true)
    @Description(shortDefinition = "", formalDefinition = "")
    private StringType position;

    @JsonBackReference
    @Child(name = "paymentMode", type = {
            Coding.class }, order = 32, min = 0, max = 1, modifier = false, summary = true)
    private Coding paymentMode;

    @JsonBackReference
    @Child(name = "LMP", type = {
            StringType.class }, order = 33, min = 0, max = 1, modifier = false, summary = true)
    private StringType LMP;

    @JsonBackReference
    @Child(name = "requirement", type = {
            StringType.class }, order = 34, min = 0, max = 1, modifier = false, summary = true)
    private StringType requirement;

    @Child(name = "bladesOrSuturesList", type = {}, order = 35, min = 0, max = -1, modifier = false, summary = true)
    @Description(shortDefinition = "", formalDefinition = "")
    private List<PreferenceKartItemsComponent> bladesOrSuturesList;

    @Child(name = "instrumentSetsList", type = {}, order = 36, min = 0, max = Child.MAX_UNLIMITED, modifier = false, summary = true)
    @Description(shortDefinition = "", formalDefinition = "")
    private List<PreferenceKartItemsComponent> instrumentSetsList;

    @Child(name = "equipmentsList", type = {}, order = 37, min = 0, max = Child.MAX_UNLIMITED, modifier = false, summary = true)
    @Description(shortDefinition = "", formalDefinition = "")
    private List<PreferenceKartItemsComponent> equipmentsList;

    @Child(name = "consumablesList", type = {}, order = 38, min = 0, max = Child.MAX_UNLIMITED, modifier = false, summary = true)
    @Description(shortDefinition = "", formalDefinition = "")
    private List<PreferenceKartItemsComponent> consumablesList;

    @Child(name = "implantsList", type = {}, order = 39, min = 0, max = Child.MAX_UNLIMITED, modifier = false, summary = true)
    @Description(shortDefinition = "", formalDefinition = "")
    private List<PreferenceKartItemsComponent> implantsList;

    @Child(name = "servicesList", type = {}, order = 40, min = 0, max = Child.MAX_UNLIMITED, modifier = false, summary = true)
    @Description(shortDefinition = "", formalDefinition = "")
    private List<PreferenceKartItemsComponent> servicesList;

    public StringType getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(StringType businessStatus) {
        this.businessStatus = businessStatus;
    }

    public Coding getAnesthesiaType() {
        return anesthesiaType;
    }

    public void setAnesthesiaType(Coding anesthesiaType) {
        this.anesthesiaType = anesthesiaType;
    }

    public Coding getLaterality() {
        return laterality;
    }

    public void setLaterality(Coding laterality) {
        this.laterality = laterality;
    }

    public StringType getPreparation() {
        return preparation;
    }

    public void setPreparation(StringType preparation) {
        this.preparation = preparation;
    }

    public StringType getPosition() {
        return position;
    }

    public void setPosition(StringType position) {
        this.position = position;
    }

    public Coding getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(Coding paymentMode) {
        this.paymentMode = paymentMode;
    }

    public StringType getLMP() {
        return LMP;
    }

    public void setLMP(StringType LMP) {
        this.LMP = LMP;
    }

    public StringType getRequirement() {
        return requirement;
    }

    public void setRequirement(StringType requirement) {
        this.requirement = requirement;
    }

    public List<PreferenceKartItemsComponent> getBladesOrSuturesList() {
        return bladesOrSuturesList;
    }

    public void setBladesOrSuturesList(List<PreferenceKartItemsComponent> bladesOrSuturesList) {
        this.bladesOrSuturesList = bladesOrSuturesList;
    }

    public List<PreferenceKartItemsComponent> getInstrumentSetsList() {
        return instrumentSetsList;
    }

    public void setInstrumentSetsList(List<PreferenceKartItemsComponent> instrumentSetsList) {
        this.instrumentSetsList = instrumentSetsList;
    }

    public List<PreferenceKartItemsComponent> getEquipmentsList() {
        return equipmentsList;
    }

    public void setEquipmentsList(List<PreferenceKartItemsComponent> equipmentsList) {
        this.equipmentsList = equipmentsList;
    }

    public List<PreferenceKartItemsComponent> getConsumablesList() {
        return consumablesList;
    }

    public void setConsumablesList(List<PreferenceKartItemsComponent> consumablesList) {
        this.consumablesList = consumablesList;
    }

    public List<PreferenceKartItemsComponent> getImplantsList() {
        return implantsList;
    }

    public void setImplantsList(List<PreferenceKartItemsComponent> implantsList) {
        this.implantsList = implantsList;
    }

    public List<PreferenceKartItemsComponent> getServicesList() {
        return servicesList;
    }

    public void setServicesList(List<PreferenceKartItemsComponent> servicesList) {
        this.servicesList = servicesList;
    }

    @Block
    public static class PreferenceKartItemsComponent extends BackboneElement implements IBaseBackboneElement {

        private static final long serialVersionUID = 4456633L;

        @Child(name = "code", type = {
                StringType.class }, order = 8, min = 0, max = 1, modifier = false, summary = true)
        protected StringType code;

        @Child(name = "itemId", type = {
                IntegerType.class }, order = 1, min = 0, max = 1, modifier = false, summary = true)
        protected IntegerType itemId;

        @Child(name = "desc", type = {
                StringType.class }, order = 2, min = 0, max = 1, modifier = false, summary = true)
        protected StringType desc;

        @Child(name = "type", type = {
                StringType.class }, order = 4, min = 0, max = 1, modifier = false, summary = true)
        protected StringType type;

        @Child(name = "priority", type = {
                StringType.class }, order = 5, min = 0, max = 1, modifier = false, summary = true)
        protected StringType priority;

        @Child(name = "group", type = {
                StringType.class }, order = 6, min = 0, max = 1, modifier = false, summary = true)
        protected StringType group;

        @Child(name = "groupName", type = {
                StringType.class }, order = 7, min = 0, max = 1, modifier = false, summary = true)
        protected StringType groupName;

        @Child(name = "requestedQuantity", type = {
                PositiveIntType.class }, order = 3, min = 0, max = 1, modifier = false, summary = true)
        protected PositiveIntType requestedQuantity;

        @Child(name = "utilisedQuantity", type = {
                PositiveIntType.class }, order = 9, min = 0, max = 1, modifier = false, summary = true)
        protected PositiveIntType utilisedQuantity;

        @Child(name = "unUtilisedQuantity", type = {
                PositiveIntType.class }, order = 10, min = 0, max = 1, modifier = false, summary = true)
        protected PositiveIntType unUtilisedQuantity;

        public IntegerType getItemId() {
            return itemId;
        }

        public void setItemId(IntegerType itemId) {
            this.itemId = itemId;
        }

        public StringType getDesc() {
            return desc;
        }

        public void setDesc(StringType desc) {
            this.desc = desc;
        }

        public StringType getType() {
            return type;
        }

        public void setType(StringType type) {
            this.type = type;
        }

        public StringType getPriority() {
            return priority;
        }

        public void setPriority(StringType priority) {
            this.priority = priority;
        }

        public StringType getGroup() {
            return group;
        }

        public void setGroup(StringType group) {
            this.group = group;
        }

        public StringType getGroupName() {
            return groupName;
        }

        public void setGroupName(StringType groupName) {
            this.groupName = groupName;
        }

        public StringType getCode() {
            return code;
        }

        public void setCode(StringType code) {
            this.code = code;
        }

        public PositiveIntType getRequestedQuantity() {
            return requestedQuantity;
        }

        public void setRequestedQuantity(PositiveIntType requestedQuantity) {
            this.requestedQuantity = requestedQuantity;
        }

        public PositiveIntType getUtilisedQuantity() {
            return utilisedQuantity;
        }

        public void setUtilisedQuantity(PositiveIntType utilisedQuantity) {
            this.utilisedQuantity = utilisedQuantity;
        }

        public PositiveIntType getUnUtilisedQuantity() {
            return unUtilisedQuantity;
        }

        public void setUnUtilisedQuantity(PositiveIntType unUtilisedQuantity) {
            this.unUtilisedQuantity = unUtilisedQuantity;
        }

        @Override
        public PreferenceKartItemsComponent copy() {
            PreferenceKartItemsComponent prfKItem = new PreferenceKartItemsComponent();
            copyValues(prfKItem);
//	            if (type != null) {
//	            	prfKItem.type = new ArrayList<StringType>();
//	                for (StringType i : type)
//	                	prfKItem.type.add(i.copy());
//	            }
            prfKItem.code = code == null ? null : code.copy();
            prfKItem.desc = desc == null ? null : desc.copy();
            prfKItem.group = group == null ? null : group.copy();
            prfKItem.groupName = groupName == null ? null : groupName.copy();
            prfKItem.itemId = itemId == null ? null : itemId.copy();
            prfKItem.priority = code == null ? null : code.copy();
            prfKItem.type = code == null ? null : code.copy();
            prfKItem.requestedQuantity = requestedQuantity == null ? null : requestedQuantity.copy();
            prfKItem.utilisedQuantity = utilisedQuantity == null ? null : utilisedQuantity.copy();
            prfKItem.unUtilisedQuantity = unUtilisedQuantity == null ? null : unUtilisedQuantity.copy();

            return prfKItem;
        }

        public PreferenceKartItemsComponent() {
            super();
            // TODO Auto-generated constructor stub
        }

        protected void listChildren(List<Property> children) {
            super.listChildren(children);
            children.add(new Property("code", "string", "", 0, 1, code));
            children.add(new Property("desc", "string", "", 0, 1, desc));
            children.add(new Property("itemId", "string", "", 0, 1, itemId));
            children.add(new Property("priority", "string", "", 0, 1, priority));
            children.add(new Property("type", "string", "", 0, 1, type));
            children.add(new Property("group", "string", "", 0, 1, group));
            children.add(new Property("groupName", "string", "", 0, 1, groupName));
            children.add(new Property("requestedQuantity", "positiveInt", "", 0, 1, requestedQuantity));
            children.add(new Property("utilisedQuantity", "positiveInt", "", 0, 1, utilisedQuantity));
            children.add(new Property("unUtilisedQuantity", "positiveInt", "", 0, 1, unUtilisedQuantity));

        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
            switch (_hash) {
                case -1718432877:
                    /* code */ return new Property("code", "string", "", 0, 1, code);
                case -1718432899:
                    /* desc */ return new Property("desc", "string", "", 0, 1, desc);
                case -1718432844:
                    /* itemId */ return new Property("itemId", "string", "", 0, 1, itemId);
                case -1718432833:
                    /* priority */ return new Property("priority", "string", "", 0, 1, priority);
                case -1718432822:
                    /* type */ return new Property("type", "string", "", 0, 1, type);
                case -1718432855:
                    /* group */ return new Property("group", "string", "", 0, 1, group);
                case -1718432866:
                    /* groupName */ return new Property("groupName", "string", "", 0, 1, groupName);
                case -1718432817:
                    /* requestedQuantity */ return new Property("requestedQuantity", "positiveInt", "", 0, 1, requestedQuantity);
                case -1718432814:
                    /* utilisedQuantity */ return new Property("utilisedQuantity", "positiveInt", "", 0, 1, utilisedQuantity);
                case -1718432813:
                    /* unUtilisedQuantity */ return new Property("unUtilisedQuantity", "positiveInt", "", 0, 1, unUtilisedQuantity);
                default:
                    return super.getNamedProperty(_hash, _name, _checkValid);
            }

        }

        @Override
        public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
            switch (hash) {
                case -1718432877:
                    /* code */ return this.code == null ? new Base[0] : new Base[] { this.code }; // code
                case -1718432899:
                    /* desc */ return this.desc == null ? new Base[0] : new Base[] { this.desc }; // desc
                case -1718432844:
                    /* itemId */ return this.itemId == null ? new Base[0] : new Base[] { this.itemId }; // itemId
                case -1718432833:
                    /* priority */ return this.priority == null ? new Base[0] : new Base[] { this.priority }; // priority
                case -1718432822:
                    /* type */ return this.type == null ? new Base[0] : new Base[] { this.type }; // type
                case -1718432855:
                    /* group */ return this.group == null ? new Base[0] : new Base[] { this.group }; // group
                case -1718432866:
                    /* groupName */ return this.groupName == null ? new Base[0] : new Base[] { this.groupName }; // groupName
                case -1718432817:
                    /* groupName */ return this.requestedQuantity == null ? new Base[0] : new Base[] { this.requestedQuantity }; // requestedQuantity
                case -1718432815:
                    /* groupName */ return this.utilisedQuantity == null ? new Base[0] : new Base[] { this.utilisedQuantity }; // utilisedQuantity
                case -1718432813:
                    /* groupName */ return this.unUtilisedQuantity == null ? new Base[0] : new Base[] { this.unUtilisedQuantity }; // unUtilisedQuantity
                default:
                    return super.getProperty(hash, name, checkValid);
            }

        }

        @Override
        public Base setProperty(int hash, String name, Base value) throws FHIRException {
            switch (hash) {
                case -1718432877: // code
                    this.code = castToString(value); // code
                case -1718432899: // desc
                    this.desc = castToString(value); // desc
                case -1718432844: // itemId
                    this.itemId = castToInteger(value); // itemId
                case -1718432833: // priority
                    this.priority = castToString(value); // priority
                case -1718432822: // type
                    this.type = castToString(value); // type
                case -1718432855: // group
                    this.group = castToString(value); // group
                case -1718432866: // groupName
                    this.groupName = castToString(value); // groupName
                case -1718432817: // groupName
                    this.requestedQuantity = castToPositiveInt(value); // requestedQuantity
                case -1718432815: // groupName
                    this.utilisedQuantity = castToPositiveInt(value); // utilisedQuantity
                case -1718432813: // groupName
                    this.unUtilisedQuantity = castToPositiveInt(value); // unUtilisedQuantity
                default:
                    return super.setProperty(hash, name, value);
            }

        }

        @Override
        public Base setProperty(String name, Base value) throws FHIRException {
            if (name.equals("code")) {
                this.code = castToString(value); // StringType
            } else if (name.equals("desc")) {
                this.desc = castToString(value); // StringType
            } else if (name.equals("itemId")) {
                this.itemId = castToInteger(value); // IntegerType
            } else if (name.equals("priority")) {
                this.priority = castToString(value); // StringType
            } else if (name.equals("type")) {
                this.type = castToString(value); // StringType
            } else if (name.equals("group")) {
                this.group = castToString(value); // StringType
            } else if (name.equals("groupName")) {
                this.groupName = castToString(value); // StringType
            }
            else if (name.equals("requestedQuantity")) {
                this.requestedQuantity = castToPositiveInt(value); // IntegerType
            }
            else if (name.equals("utilisedQuantity")) {
                this.utilisedQuantity = castToPositiveInt(value); // IntegerType
            }
            else if (name.equals("unUtilisedQuantity")) {
                this.unUtilisedQuantity = castToPositiveInt(value); // IntegerType
            }else
                return super.setProperty(name, value);
            return value;
        }

        @Override
        public Base makeProperty(int hash, String name) throws FHIRException {
            switch (hash) {
                case -1718432877: // code
                    return getCode();
                case -1718432899: // desc
                    return getDesc();
                case -1718432844: // itemId
                    return getItemId();
                case -1718432833: // priority
                    return getPriority();
                case -1718432822: // type
                    return getType();
                case -1718432855: // group
                    return getGroup();
                case -1718432866: // groupName
                    return getGroupName();
                case -1718432817: // requestedQuantity
                    return getRequestedQuantity();
                case -1718432815: // utilisedQuantity
                    return getUtilisedQuantity();
                case -1718432813: // unUtilisedQuantity
                    return getUnUtilisedQuantity();
                default:
                    return super.makeProperty(hash, name);
            }

        }

        @Override
        public String[] getTypesForProperty(int hash, String name) throws FHIRException {
            switch (hash) {
                case -1718432877: // code
                case -1718432899: // desc
                case -1718432833: // priority
                case -1718432822: // type
                case -1718432855: // group
                case -1718432866: // groupName
                    return new String[] { "string" };
                case -1718432844: // itemId
                case -1718432817: // requestedQuantity
                case -1718432815: // utilisedQuantity
                case -1718432813: // unUtilisedQuantity
                    return new String[] { "integer" };
                default:
                    return super.getTypesForProperty(hash, name);
            }

        }

        @Override
        public Base addChild(String name) throws FHIRException {
            if (name.equals("code") || name.equals("desc") || name.equals("itemId") || name.equals("quantity")
                    || name.equals("priority") || name.equals("group") || name.equals("groupName")
                    || name.equals("type")|| name.equals("requestedQuantity")|| name.equals("utilisedQuantity")
                    || name.equals("unUtilisedQuantity")) {
                throw new FHIRException("Cannot call addChild on a primitive type Contract.text");
            } else
                return super.addChild(name);
        }

        @Override
        public boolean equalsDeep(Base other_) {
            if (!super.equalsDeep(other_))
                return false;
            if (!(other_ instanceof PreferenceKartItemsComponent))
                return false;
            PreferenceKartItemsComponent o = (PreferenceKartItemsComponent) other_;
            return compareDeep(code, o.code, true) && compareDeep(desc, o.desc, true)
                    && compareDeep(requestedQuantity, o.requestedQuantity, true); // FIXME: put proper compare logic
        }

        @Override
        public boolean equalsShallow(Base other_) {
            if (!super.equalsShallow(other_))
                return false;
            if (!(other_ instanceof PreferenceKartItemsComponent))
                return false;
            PreferenceKartItemsComponent o = (PreferenceKartItemsComponent) other_;
            return true;
        }

        public boolean isEmpty() {
            return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, desc, type, itemId,priority, group,groupName
                    ,requestedQuantity,utilisedQuantity,unUtilisedQuantity);
        }

        public String fhirType() {
            return "ExtendedServiceRequest.PreferenceKartItems";

        }

    }

}
