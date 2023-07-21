package com.app.toko.viewmodels;
import android.app.Application;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;


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
    private MutableLiveData<String> location;

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
        location = new MutableLiveData<>();

        cities = new MutableLiveData<>();
        districts = new MutableLiveData<>();
        wards = new MutableLiveData<>();
    }

    public void registerAddress(){
        Contact mcontact = new Contact(
                sdt.getValue(),
                tp.getValue(),
                quan.getValue(),
                phuong.getValue(),
                duong.getValue());

        contact.postValue(mcontact);
        if (contact == null || contact.getValue() == null)
        {
            Log.d("Contact", " NULL");
            return;
        }
        contactRepository.RegisterContact(contact);
    }

    public void loadCities() {
        addressRepository.LoadCities();
        addressRepository.getCityLiveData().observeForever(new Observer<Map<String, City>>() {
            @Override
            public void onChanged(Map<String, City> cityMap) {
                if (cityMap != null) {
                    List<String> cityNames = new ArrayList<>();
                    for (City city : cityMap.values()) {
                        cityNames.add(city.getName_with_type());
                    }
                    cities.setValue(cityNames.toArray(new String[0]));

                    // Xóa dữ liệu quận và phường khi thay đổi thành phố
                    districts.setValue(new String[0]);
                    wards.setValue(new String[0]);
                } else {
                    cities.postValue(new String[0]); // Hoặc trả về null hoặc thông báo lỗi tùy vào trường hợp thực tế.
                }
                addressRepository.getCityLiveData().removeObserver(this);
            }
        });


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
        addressRepository.getDistrictLiveData().observeForever(new Observer<Map<String, District>>() {
            @Override
            public void onChanged(Map<String, District> districtMap) {
                if (districtMap != null) {
                    List<String> districtNames = new ArrayList<>();
                    for (District district : districtMap.values()) {
                        districtNames.add(district.getName_with_type());
                    }
                    districts.setValue(districtNames.toArray(new String[0]));

                    // Xóa dữ liệu phường khi thay đổi quận
                    wards.setValue(new String[0]);
                } else {
                    districts.postValue(new String[0]);
                }
                addressRepository.getDistrictLiveData().removeObserver(this);
            }
        });
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
        addressRepository.getWardsLiveData().observeForever(new Observer<Map<String, Wards>>() {
            @Override
            public void onChanged(Map<String, Wards> wardsMap) {
                if (wardsMap != null) {
                    List<String> wardsNames = new ArrayList<>();
                    for (Wards wards : wardsMap.values()) {
                        wardsNames.add(wards.getName_with_type());
                    }
                    wards.setValue(wardsNames.toArray(new String[0]));
                } else {
                    wards.setValue(new String[0]);
                }
                addressRepository.getWardsLiveData().removeObserver(this);
            }
        });
    }


    public void onClickLocation(String location){
        this.location.postValue(location);
    }

    public boolean isValidName (){
        return !TextUtils.isEmpty(ht.getValue());
    }

    public  boolean isValidPhone() {
        return  !TextUtils.isEmpty(sdt.getValue())
                && Patterns.PHONE.matcher(sdt.getValue()).matches()
                && (sdt.getValue().length()==10);
    }

    public boolean isValidCity (){
        return !TextUtils.isEmpty(tp.getValue());
    }

    public  boolean isValidDistrict() {
        return !TextUtils.isEmpty(quan.getValue());
    }

    public  boolean isValidWards() {

        return !TextUtils.isEmpty(phuong.getValue());
    }

    public  boolean isValidAddress() {
        return !TextUtils.isEmpty(duong.getValue());
    }

    public  boolean isValidLocation()
    {
        return !TextUtils.isEmpty(location.getValue());
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

    public LiveData<Contact> getContact() {
        return contact;
    }


}

