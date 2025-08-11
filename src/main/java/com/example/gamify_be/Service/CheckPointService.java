package com.example.gamify_be.Service;

import com.example.gamify_be.Dto.ApiResponse.ApiResponse;
import com.example.gamify_be.Dto.CheckPoint.CPRequestDto;
import com.example.gamify_be.Dto.CheckPoint.CPPositionRequestDto;
import com.example.gamify_be.Entity.CheckPoint;
import com.example.gamify_be.Entity.Journey;
import com.example.gamify_be.Entity.User;
import com.example.gamify_be.Repository.CheckPointRepository;
import com.google.protobuf.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CheckPointService {
    @Autowired
    CheckPointRepository checkPointRepository;

    @Autowired
    UserService userService;

    @Autowired
    JourneyService journeyService;

    //Hàm kiểm tra ải tồn tại hay không bằng id
    public boolean isExistedById(String id){
        return checkPointRepository.findById(id).isPresent();
    }

    //Hàm tìm ải bằng ID
    private CheckPoint getById(String id){
        return checkPointRepository.findById(id).orElse(null);
    }

    //Hàm kiểm tra tên ải đã tồn tại hay không
    private boolean isNameExisted(String name){
        return checkPointRepository.findByName(name).isPresent();
    };

    //Hàm lấy dữ liệu các ải cùng 1 lộ trình , cùng 1 màn
    private List<CheckPoint> getAllCPByJourneyAndSection(String JourneyId, Integer section){
        return checkPointRepository.findAllByJourneyIdAndSection(JourneyId,section);
    }

    //Hàm tìm đối tượng theo thứ tự trong danh sách CP
    private CheckPoint getCPByOrd(List<CheckPoint> checkPoints,Integer ord){
        for (CheckPoint cp : checkPoints){
            if (Objects.equals(cp.getOrd(), ord)){
                return cp;
            }
        }
        return null;
    }

    //Hàm lấy ra max oder trong danh sách trừ bản thân đối tượng
    private Integer getMaxOrderInList(List<CheckPoint> checkPoints,String CPId){
        List<CheckPoint> CPList = checkPoints.stream()
                .filter(item -> !Objects.equals(item.getId(), CPId))
                .toList();
        Integer max = CPList.getFirst().getOrd();
        for (CheckPoint cp:CPList){
            if (cp.getOrd() != null){
                if (cp.getOrd() > max){
                    max = cp.getOrd();
                }
            }
        }
        if (max == null){
            return 0;
        }
        return max;
    }

    //Hàm lấy ra max oder trong danh sách
    private Integer getMaxOrderInList(List<CheckPoint> checkPoints){
        Integer max = checkPoints.getFirst().getOrd();
        for (CheckPoint cp:checkPoints){
            if (cp.getOrd() != null){
                if (cp.getOrd() > max){
                    max = cp.getOrd();
                }
            }
        }
        if (max == null){
            return 0;
        }
        return max;
    }

    //Hàm sắp xếp lại thứ tự trong danh sách ải sau khi xoá 1 phần tử
    //Từ vị trí phần tử xoá , dười các phần tử có thứ tự sau phần tử xoá lên 1 đơn vị
    private void sortCPListOrder(List<CheckPoint> checkPoints, Integer removedOrd, String operator){
        for (CheckPoint cp: checkPoints){
            if (cp.getOrd() > removedOrd){
                cp.setOrd(cp.getOrd() - 1);
                cp.setUpdated_by(operator);
            }
        }
    }

    //Tạo ải mới
    public ApiResponse<CheckPoint> createCP(CPRequestDto req){
        //Kiểm tra trường tên ải không được để trống
        if (req.getName() == null){
            return ApiResponse.error("Tên ải không được để trống", HttpStatus.BAD_REQUEST);
        }

        //Kiểm tra người thao tác có tồn tại không
        User operator = userService.getUserById(req.getOperator());
        if (operator == null){
            return ApiResponse.error("Không tìm thấy người thao tác tương ứng", HttpStatus.NOT_FOUND);
        };

        //Tạo đối tượng ải mới
        CheckPoint nCP = new CheckPoint(req.getName(),req.getDescription(),operator.getUsername());

        //Lưu xuống CSDL
        try{
            checkPointRepository.save(nCP);
            return ApiResponse.success("Tạo ải thành công",nCP);
        }catch(Exception e){
            return ApiResponse.error("Lỗi trong quá trình lưu ải mới : "+ e,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Cập nhật nội dung ải
    public ApiResponse<CheckPoint> updateCP(String id, CPRequestDto req){
        //Tìm đối tượng ải
        CheckPoint checkPoint = getById(id);
        if (checkPoint == null){
            return ApiResponse.error("Không tìm thấy đối tượng ải tương ứng", HttpStatus.NOT_FOUND);
        }

        //Kiểm tra trường tên ải không được để trống
        if (req.getName() != null){
            //Kiểm tra tên ải có bị trùng không
            if (isNameExisted(req.getName())){
                return ApiResponse.error("Tên ải đã tồn tại", HttpStatus.CONFLICT);
            }
            checkPoint.setName(req.getName());
        }



        //Cập nhật mô tả nếu có
        if (req.getDescription() != null){
            checkPoint.setDescription(req.getDescription());
        }

        //Kiểm tra trường người thao tác
        if (req.getOperator() == null){
            return ApiResponse.error("Yêu cầu chưa cung cấp người thao tác",HttpStatus.BAD_REQUEST);
        }

        //Kiểm tra người thao tác có tồn tại không
        User operator = userService.getUserById(req.getOperator());
        if (operator == null){
            return ApiResponse.error("Không tìm thấy người thao tác tương ứng", HttpStatus.NOT_FOUND);
        };
        checkPoint.setUpdated_by(operator.getUsername());
        checkPointRepository.save(checkPoint);
        return ApiResponse.success("Cập nhật thông tin ải thành công",checkPoint);
    }

    //Thiết lập ải vào lộ trình và màn
    public ApiResponse<CheckPoint> setPosition(String id,CPPositionRequestDto req){
        //Tìm kiếm đối tượng ải tương ứng
        CheckPoint checkPoint = getById(id);
        if (checkPoint == null){
            return ApiResponse.error("Không tìm được ải tương ứng", HttpStatus.NOT_FOUND);
        }

        //Tìm kiếm đối tượng lộ trình tương ứng
        //Trường nhập có giá trị => tìm xác thực id lộ trình và cập nhật
        if (req.getJourneyId() != null){
            Journey journey = journeyService.getJourneyById(req.getJourneyId());
            if (journey == null){
                return ApiResponse.error("Không tìm thấy lộ trình với ID tương ứng", HttpStatus.NOT_FOUND);
            }
            checkPoint.setJourney_id(req.getJourneyId());
        }else{
            //Trường không có giá trị => cập nhật id lộ trình thành null
            checkPoint.setJourney_id(null);
        }

        //Cập nhật màn chơi
        checkPoint.setSection(req.getSection());

        //Kiểm tra người thao tác có tồn tại không
        User operator = userService.getUserById(req.getOperator());
        if (operator == null){
            return ApiResponse.error("Không tìm thấy người thao tác tương ứng", HttpStatus.NOT_FOUND);
        };
        checkPoint.setUpdated_by(operator.getUsername());

        //Cập nhật dữ liệu xuống CSDL
        try{
            checkPointRepository.save(checkPoint);
            return ApiResponse.success("Đã cập nhật vị trí của ải",checkPoint);
        }catch(Exception e){
            return ApiResponse.error("Lỗi trong quá trình cập nhật vị trí ải", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Cập nhật thứ tự ải
    public ApiResponse<CheckPoint> setCPOrder(String id, Integer ord, String operatorId){
        //Tìm ra đối tượng ải tương ứng
        CheckPoint checkPoint = getById(id);
        if (checkPoint == null){
            return ApiResponse.error("Không tìm thấy ải tương ứng",HttpStatus.NOT_FOUND);
        }

        //Kiểm tra trường người thao tác
        if (operatorId == null){
            return ApiResponse.error("Yêu cầu chưa cung cấp người thao tác",HttpStatus.BAD_REQUEST);
        }

        //Kiểm tra người thao tác có tồn tại không
        User operator = userService.getUserById(operatorId);
        if (operator == null){
            return ApiResponse.error("Không tìm thấy người thao tác tương ứng", HttpStatus.NOT_FOUND);
        };

        //Tìm ra danh sách các ải chung lộ trình và màn với ải đối tượng
        List<CheckPoint> checkPointList = getAllCPByJourneyAndSection(checkPoint.getJourney_id(),checkPoint.getSection());
        //Kiểm tra nếu thứ tự đích đã được sử dụng chưa
        CheckPoint checkPointTemp = getCPByOrd(checkPointList,ord);
        if (checkPointTemp == null){
            //Chưa được sử dụng => tức xếp đối tượng vào cuối hàng
            Integer maxOrd = getMaxOrderInList(checkPointList,checkPoint.getId());
            System.out.println(maxOrd);
            checkPoint.setOrd(maxOrd + 1);
            checkPoint.setUpdated_by(operator.getUsername());
            try{
                checkPointRepository.save(checkPoint);
                return ApiResponse.success("Cập nhật thứ tự thành công",checkPoint);
            }catch(Exception e){
                return ApiResponse.error("Lỗi trong quá trình cập nhật thứ tự ải : " +e,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            //Đã được sử dụng => hoán đổi vị trị 2 đối tượng và lưu lại
            Integer ordTemp = checkPoint.getOrd();
            checkPoint.setOrd(checkPointTemp.getOrd());
            checkPoint.setUpdated_by(operator.getUsername());
            checkPointTemp.setOrd(ordTemp);
            checkPointTemp.setUpdated_by(operator.getUsername());
            try{
                checkPointRepository.saveAll(Arrays.asList(checkPoint,checkPointTemp));
                return ApiResponse.success("Cập nhật thứ tự thành công",checkPoint);
            }catch(Exception e){
                return ApiResponse.error("Lỗi trong quá trình cập nhật thứ tự ải : " +e,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    };

    //Huỷ thứ tự ải
    public ApiResponse<CheckPoint>  removeCPOrder(String id, String operatorId){
        //Tìm ra đối tượng ải tương ứng
        CheckPoint checkPoint = getById(id);
        if (checkPoint == null){
            return ApiResponse.error("Không tìm thấy ải tương ứng",HttpStatus.NOT_FOUND);
        }

        //Kiểm tra trường người thao tác
        if (operatorId == null){
            return ApiResponse.error("Yêu cầu chưa cung cấp người thao tác",HttpStatus.BAD_REQUEST);
        }

        //Kiểm tra người thao tác có tồn tại không
        User operator = userService.getUserById(operatorId);
        if (operator == null){
            return ApiResponse.error("Không tìm thấy người thao tác tương ứng", HttpStatus.NOT_FOUND);
        };

        //Tìm ra danh sách các ải chung lộ trình và màn với ải đối tượng
        //(danh sách loại trừ đối tượng ải hiện tại và các ải chưa xếp thứ tự)
        List<CheckPoint> checkPointList = new ArrayList<>(getAllCPByJourneyAndSection(checkPoint.getJourney_id(), checkPoint.getSection()).stream()
                .filter(item -> !Objects.equals(item.getId(), checkPoint.getId()) && !Objects.equals(item.getOrd(), null))
                .toList());

        //Sắp xếp lại thứ tự của của ải
        sortCPListOrder(checkPointList, checkPoint.getOrd(),operator.getUsername());
        //Đặt lại thứ tại ải về rỗng
        checkPoint.setOrd(null);
        checkPoint.setUpdated_by(operator.getUsername());
        //Thêm lại vào danh sách cập nhật
        checkPointList.add(checkPoint);
        //Cập nhật vào CSDL
        try{
            checkPointRepository.saveAll(checkPointList);
            return ApiResponse.success("Huỷ thứ tự ải thành công",checkPoint);
        }catch(Exception e){
            return ApiResponse.error("Lỗi trong quá trình huỷ thứ tự ải : " + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Lấy tất cả dữ liệu ải
    public ApiResponse<List<CheckPoint>> getAllCP() {
        return ApiResponse.success("Lấy dữ liệu ải thành công",checkPointRepository.findAll());
    }

    //Lấy dữ liệu ải theo id
    public ApiResponse<CheckPoint> getCPById(String id){
        CheckPoint cp = getById(id);
        if (cp == null){
            return ApiResponse.error("ID ải không tồn tại",HttpStatus.NOT_FOUND);
        }
        return ApiResponse.success("Lấy thông tin ải theo id thành công",cp);
    }
}
