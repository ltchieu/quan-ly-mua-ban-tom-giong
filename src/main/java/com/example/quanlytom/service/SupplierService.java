package com.example.quanlytom.service;

import com.example.quanlytom.dto.request.SupplierCreationRequest;
import com.example.quanlytom.dto.response.SupplierDetailResponse;
import com.example.quanlytom.dto.response.SupplierNameResponse;
import com.example.quanlytom.dto.response.SupplierPageResponse;
import com.example.quanlytom.dto.response.SupplierResponse;
import com.example.quanlytom.entity.Import;
import com.example.quanlytom.entity.Supplier;
import com.example.quanlytom.mapper.SupplierMapper;
import com.example.quanlytom.repository.ImportRepository;
import com.example.quanlytom.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService {
    final SupplierRepository supplierRepository;
    final SupplierMapper supplierMapper;
    final ImportRepository importRepository;

    public List<SupplierNameResponse> getAllSupplierName() {
        return supplierMapper.toSupplierNameResponseList(supplierRepository.findAll());
    }

    public SupplierPageResponse getAllSupplier(int page, int size, String name){
        Pageable paging = PageRequest.of(page, size);

        Page<Supplier> supplierPage = supplierRepository.findAllByFullName(name, paging);
        List<SupplierResponse> suppliers = supplierMapper.toSupplierResponseList(supplierPage.getContent());

        return new SupplierPageResponse(supplierPage.getTotalElements(), supplierPage.getNumber(), supplierPage.getTotalPages(), suppliers);
    }

    public SupplierDetailResponse getDetailSupplier(Integer id){
        Supplier supplier = supplierRepository.findById(id).orElseThrow(() -> new RuntimeException("Supplier not found"));
        List<Import> imports = importRepository.findBySupplier_Id(id);
        List<SupplierDetailResponse.ImportFromSupplier> importFromSupplier = supplierMapper.toImportFromSupplierList(imports);

        return new SupplierDetailResponse(supplierMapper.toSupplierResponse(supplier), importFromSupplier);
    }

    public SupplierResponse createSupplier(SupplierCreationRequest supplierCreationRequest){
        Supplier newSupplier = supplierMapper.toNewSupplier(supplierCreationRequest);
        newSupplier.setDeleted(false);

        supplierRepository.save(newSupplier);

        return supplierMapper.toSupplierResponse(newSupplier);
    }

    public SupplierResponse updateSupplier(SupplierCreationRequest supplierUpdateRequest, Integer supplierId){
        Supplier anSupplier = supplierRepository.findById(supplierId).orElseThrow(() -> new RuntimeException("Supplier not found"));

        supplierMapper.updateSupplierFromRequest(supplierUpdateRequest, anSupplier);
        supplierRepository.save(anSupplier);

        return supplierMapper.toSupplierResponse(anSupplier);
    }
}
