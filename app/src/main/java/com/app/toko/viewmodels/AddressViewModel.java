package com.app.toko.viewmodels;
import android.app.Application;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;


import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;


import com.app.toko.models.City;
import com.app.toko.models.Contact;
import com.app.toko.models.District;
import com.app.toko.models.Wards;
import com.app.toko.repositories.AddressRepository;
import com.app.toko.repositories.ContactRepository;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddressViewModel extends AndroidViewModel {

    private MutableLiveData<Contact> contact;
    private ContactRepository contactRepository;
    private AddressRepository addressRepository;

    public MutableLiveData<String> ht;
    public MutableLiveData<String> sdt;
    public MutableLiveData<String> tp;
    public MutableLiveData<String> quan;
    public MutableLiveData<String> phuong;
    public MutableLiveData<String> duong;
    public MutableLiveData<Boolean> isDefault;

    private MutableLiveData<String[]> cities;
    private MutableLiveData<String[]> districts;
    private MutableLiveData<String[]> wards;



    public AddressViewModel(Application application) {
        super(application);
        contactRepository = new ContactRepository(getApplication());
        addressRepository = new AddressRepository(getApplication());

        contact = new MutableLiveData<>();
        ht = new MutableLiveData<>();
        sdt = new MutableLiveData<>();
        tp = new MutableLiveData<>();
        quan = new MutableLiveData<>();
        phuong = new MutableLiveData<>();
        duong = new MutableLiveData<>();
        isDefault = new MutableLiveData<>(false);

        cities = new MutableLiveData<>();
        districts = new MutableLiveData<>();
        wards = new MutableLiveData<>();
    }

    public void registerAddress(String id){
        Contact mcontact = new Contact(
                id,
                sdt.getValue(),
                ht.getValue(),
                tp.getValue(),
                quan.getValue(),
                phuong.getValue(),
                duong.getValue(),
                isDefault.getValue());
        contact.postValue(mcontact);
    }

    public void loadCities() {
        addressRepository.LoadCities();
    }

    public void loadDistricts() {
        if (tp == null || tp.getValue() == null) {
            Log.d("AddressViewModel", "tp NULL");
            return;
        }

        String selectedCity = tp.getValue();

        if (cities.getValue() == null) {
            loadCities();
            return;
        }

        Map<String, City> cityMap = addressRepository.getCityLiveData().getValue();
        if (cityMap == null) {
            Log.d("AddressViewModel", "cityMap NULL");
            return;
        }

        String cityCode = new String();
        for (City city : cityMap.values()) {
            if (selectedCity.equals(city.getName_with_type())) {
                cityCode = city.getCode();
                break;
            }
        }

        if (TextUtils.isEmpty(cityCode)) {
            return;
        }

        addressRepository.LoadDistrictsByCity(cityCode);

    }

    public void loadWards() {
        if (quan == null || quan.getValue() == null) {
            Log.d("AddressViewModel", "quan NULL");
            return;
        }

        String selectedDistrict = quan.getValue();

        if (districts.getValue() == null) {
            loadDistricts();
            return;
        }

        Map<String, District> districtMap = addressRepository.getDistrictLiveData().getValue();
        if (districtMap == null) {
            Log.d("AddressViewModel", "districtMap NULL");
            return;
        }

        String districtCode = new String();
        for (District district : districtMap.values()) {
            if (selectedDistrict.equals(district.getName_with_type())) {
                districtCode = district.getCode();
                break;
            }
        }

        if (TextUtils.isEmpty(districtCode)) {
            return;
        }

        addressRepository.LoadWardsByDistrict(districtCode);
    }

    public void ConvertToCities(Map<String, City> cityMap){
        if (cityMap != null) {
            List<String> cityNames = new ArrayList<>();
            for (City city : cityMap.values()) {
                cityNames.add(city.getName_with_type());
            }
            cities.postValue(cityNames.toArray(new String[0]));

        } else {
            cities.postValue(new String[0]); // Hoặc trả về null hoặc thông báo lỗi tùy vào trường hợp thực tế.
        }
    }

    public void ConvertToDistricts(Map<String, District> districtMap){

        if (districtMap != null) {
            List<String> districtNames = new ArrayList<>();
            for (District district : districtMap.values()) {
                districtNames.add(district.getName_with_type());
            }
            districts.postValue(districtNames.toArray(new String[0]));

        } else {
            districts.postValue(new String[0]);
        }

    }

    public void ConvertToWards(Map<String, Wards> wardsMap){

        if (wardsMap != null) {
            List<String> wardsNames = new ArrayList<>();
            for (Wards wards : wardsMap.values()) {
                wardsNames.add(wards.getName_with_type());
            }
            wards.postValue(wardsNames.toArray(new String[0]));
        } else {
            wards.setValue(new String[0]);
        }
    }

    public boolean isValidName (){
        if (ht == null || ht.getValue() == null) return false;
        String name = ht.getValue().trim();
        return name.matches("^[a-zA-Z\\p{L}\\s]{6,}$");
    }

    public boolean isValidPhone()  {
        if (sdt == null || sdt.getValue() == null) return false;
        return  !TextUtils.isEmpty(sdt.getValue())
                && Patterns.PHONE.matcher(sdt.getValue()).matches()
                && (sdt.getValue().length()==10 || sdt.getValue().length()==11);
    }

    public boolean isValidCity (){
        if (tp == null || tp.getValue() == null) return false;
        return !TextUtils.isEmpty(tp.getValue().trim());
    }

    public  boolean isValidDistrict() {
        if (quan == null || quan.getValue() == null) return false;
        return !TextUtils.isEmpty(quan.getValue().trim());
    }

    public  boolean isValidWards() {
        if (phuong == null || phuong.getValue() == null) return false;
        return !TextUtils.isEmpty(phuong.getValue().trim());
    }

    public boolean isValidAddress() {
        if (duong == null || duong.getValue() == null) return false;
        String address = duong.getValue().trim();
        // Biểu thức chính quy để kiểm tra địa chỉ có hợp lệ hay không
        String regex0 = "^(?:[^,]*,){1,2}[^,]*$";
        String regex3 = "[^a-zA-Z0-9\\/\\s,À-ỹ]";

        // Kiểm tra nếu địa chỉ khớp với bất kỳ một biểu thức chính quy nào
        if (!address.matches(regex3)){
            if (!address.startsWith(",") && !address.endsWith(",")) {
               if (address.matches(regex0)) {

                    // Kiểm tra kể từ dấu phẩy cuối cùng nếu không có chữ
                    int lastCommaIndex = address.lastIndexOf(",");
                    String afterLastComma = address.substring(lastCommaIndex + 1).trim();
                    if (afterLastComma.isEmpty() || afterLastComma.matches(".*[a-zA-ZÀ-ỹ].*")) {
                        return true;
                    }  return false;

               } return false;
            } return false;
        } return false;
    }


    public boolean isValidDefault()
    {
        if (isDefault == null || isDefault.getValue() == null) isDefault.postValue(false);
        return true;
    }


    public LiveData<String[]> getCities() {
        // Kiểm tra dữ liệu đã được tải từ API chưa
        if (cities.getValue() == null) {
            loadCities();
        }
        return cities;
    }

    public LiveData<String[]> getDistricts() {
        // Kiểm tra dữ liệu đã được tải từ API chưa
        if (districts.getValue() == null) {
            loadDistricts();
        }
        return districts;
    }

    public LiveData<String[]> getWards() {
        // Kiểm tra dữ liệu đã được tải từ API chưa
        if (wards.getValue() == null) {
            loadWards();
        }
        return wards;
    }

    public MutableLiveData<Contact> getContact() {
        return contact;
    }

    public AddressRepository getAddressRepository() {
        return addressRepository;
    }

    public ContactRepository getContactRepository() {
        return contactRepository;
    }
}

