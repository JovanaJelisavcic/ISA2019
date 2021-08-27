package com.ISA2020.farmacia.entity.basic;

public class Views {

	
	public interface VerySimpleUser {
    }
    public interface  SimpleUser extends VerySimpleUser {
    }
    public interface SemiDetailedUser extends SimpleUser {
    }
    public interface DetailedUser extends SemiDetailedUser {
    }
    public interface VeryDetailedUser extends DetailedUser {
    }
    
    public interface VerySimpleFarmacy {
    }
    public interface SimpleFarmacy extends VerySimpleFarmacy {
    }
    public interface SemiDetailedFarmacy extends SimpleFarmacy {
    }
    public interface DetailedFarmacy extends SemiDetailedFarmacy {
    }
    public interface VeryDetailedFarmacy extends DetailedFarmacy {
    }
    
    
    public interface VerySimpleDrug {
    }
    public interface SimpleDrug extends VerySimpleDrug {
    }
    public interface SemiDetailedDrug extends SimpleDrug {
    }
    public interface DetailedDrug extends SemiDetailedDrug {
    }
    public interface VeryDetailedDrug extends DetailedDrug {
    }
    
    
    public interface VerySimplePrice {
    }
    public interface SimplePrice extends VerySimplePrice {
    }
    public interface SemiDetailedPrice extends SimplePrice {
    }
    public interface DetailedPrice extends SemiDetailedPrice {
    }
    public interface VeryDetailedPrice extends DetailedPrice {
    }
    
    
    public interface DrugsUnautho extends SemiDetailedDrug , SemiDetailedFarmacy, VerySimplePrice {}
    
    public interface SearchPharmacistsForPatient extends VerySimpleUser, VerySimpleFarmacy {}
    
    public interface DermaInfo extends VerySimpleUser, VerySimpleFarmacy {}
    
    public interface PricesList extends VerySimpleUser, SimplePrice, VerySimpleDrug {}
    
    public interface MyFarmacyOrdersList extends VerySimpleUser, VerySimpleFarmacy, SimpleDrug {}
    
    public interface MyFarmacyOffersList extends MyFarmacyOrdersList {}
    public interface VacationRequestsList extends VerySimpleUser{}
    public interface DermappointList extends VerySimpleUser, VerySimplePrice {}
    public interface DermappointDetailedList extends DermappointList {}
    public interface ComplaintsList extends SimpleUser {}
    public interface ReservedDrugsList extends VerySimpleDrug, VerySimpleFarmacy {}
    public interface CounselingList extends VerySimpleUser, VerySimpleFarmacy {}
    public interface PromotionList extends VerySimpleFarmacy {}
  
    
    
}
