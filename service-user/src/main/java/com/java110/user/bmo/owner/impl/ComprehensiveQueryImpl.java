package com.java110.user.bmo.owner.impl;

import com.java110.dto.RoomDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.user.bmo.owner.IComprehensiveQuery;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ComprehensiveQueryImpl implements IComprehensiveQuery {

    public static final String SEARCH_TYPE_ROOM = "1"; //根据房屋来查
    public static final String SEARCH_TYPE_OWNER_NAME = "2"; //根据业主名称查询
    public static final String SEARCH_TYPE_OWNER_TEL = "3"; //根据业主手机号
    public static final String SEARCH_TYPE_OWNER_IDCARD = "4"; //根据业主身份证
    public static final String SEARCH_TYPE_OWNER_CAR = "5"; //根据业主车牌号
    public static final String SEARCH_TYPE_OWNER_MEMBER_NAME = "6"; //根据家庭成员名称
    public static final String SEARCH_TYPE_OWNER_MEMBER_TEL = "7"; //根据家庭成员电话
    public static final String SEARCH_TYPE_OWNER_MEMBER_IDCARD = "8"; //根据家庭成员身份证

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Override
    public ResponseEntity<String> query(String communityId, String searchValue, String searchType) {
        OwnerDto ownerDto = null;
        switch (searchType) {
            case SEARCH_TYPE_ROOM:
                ownerDto = queryByRoom(communityId, searchValue);
                break;
            case SEARCH_TYPE_OWNER_NAME:
                ownerDto = queryByOwnerName(communityId, searchValue);
                break;
            case SEARCH_TYPE_OWNER_TEL:
                ownerDto = queryByOwnerTel(communityId, searchValue);
                break;
            case SEARCH_TYPE_OWNER_IDCARD:
                ownerDto = queryByOwnerIdCard(communityId, searchValue);
                break;
            case SEARCH_TYPE_OWNER_CAR:
                ownerDto = queryByOwnerCar(communityId, searchValue);
                break;
            case SEARCH_TYPE_OWNER_MEMBER_NAME:
                ownerDto = queryByOwnerMemberName(communityId, searchValue);
                break;
            case SEARCH_TYPE_OWNER_MEMBER_TEL:
                ownerDto = queryByOwnerMemberTel(communityId, searchValue);
                break;
            case SEARCH_TYPE_OWNER_MEMBER_IDCARD:
                ownerDto = queryByOwnerMemberIdCard(communityId, searchValue);
                break;
        }
        return ResultVo.createResponseEntity(1, 1, ownerDto);
    }

    /**
     * 成员身份证查询
     *
     * @param communityId
     * @param searchValue
     * @return
     */
    private OwnerDto queryByOwnerMemberIdCard(String communityId, String searchValue) {

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(communityId);
        ownerDto.setIdCard(searchValue);
        ownerDto.setOwnerTypeCds(new String[]{OwnerDto.OWNER_TYPE_CD_MEMBER, OwnerDto.OWNER_TYPE_CD_RENTING});
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
        Assert.listOnlyOne(ownerDtos, "未找到成员信息或者查询到多条，请换其他条件查询");

        return queryByOwnerId(communityId, ownerDtos.get(0).getOwnerId());
    }

    /**
     * 成员名称查询
     *
     * @param communityId
     * @param searchValue
     * @return
     */
    private OwnerDto queryByOwnerMemberTel(String communityId, String searchValue) {

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(communityId);
        ownerDto.setLink(searchValue);
        ownerDto.setOwnerTypeCds(new String[]{OwnerDto.OWNER_TYPE_CD_MEMBER, OwnerDto.OWNER_TYPE_CD_RENTING});
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
        Assert.listOnlyOne(ownerDtos, "未找到成员信息或者查询到多条，请换其他条件查询");

        return queryByOwnerId(communityId, ownerDtos.get(0).getOwnerId());
    }

    /**
     * 成员名称查询
     *
     * @param communityId
     * @param searchValue
     * @return
     */
    private OwnerDto queryByOwnerMemberName(String communityId, String searchValue) {

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(communityId);
        ownerDto.setName(searchValue);
        ownerDto.setOwnerTypeCds(new String[]{OwnerDto.OWNER_TYPE_CD_MEMBER, OwnerDto.OWNER_TYPE_CD_RENTING});
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
        Assert.listOnlyOne(ownerDtos, "未找到成员信息或者查询到多条，请换其他条件查询");

        return queryByOwnerId(communityId, ownerDtos.get(0).getOwnerId());
    }

    /**
     * 根据业主车辆查询业主
     *
     * @param communityId
     * @param searchValue
     * @return
     */
    private OwnerDto queryByOwnerCar(String communityId, String searchValue) {
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCommunityId(communityId);
        ownerCarDto.setCarNum(searchValue);
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        if (ownerCarDtos == null || ownerCarDtos.size() < 1) {
            throw new IllegalArgumentException("未查到车辆信息");
        }

        return queryByOwnerId(communityId, ownerCarDtos.get(0).getOwnerId());
    }


    /**
     * 根据业主ID查询
     *
     * @param communityId
     * @param searchValue
     * @return
     */
    private OwnerDto queryByOwnerId(String communityId, String searchValue) {
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(communityId);
        ownerDto.setOwnerId(searchValue);
        ownerDto.setOwnerTypeCd(OwnerDto.OWNER_TYPE_CD_OWNER);
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
        Assert.listOnlyOne(ownerDtos, "未找到业主信息或者查询到多条，请换其他条件查询");
        OwnerDto resOwnerDto = ownerDtos.get(0);
        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setOwnerId(ownerDtos.get(0).getOwnerId());
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);

        //没有房屋
        if (ownerRoomRelDtos == null || ownerRoomRelDtos.size() < 1) {
            return resOwnerDto;
        }
        List<String> roomIds = new ArrayList<>();
        for (OwnerRoomRelDto tmpOwnerRoomRelDto : ownerRoomRelDtos) {
            roomIds.add(tmpOwnerRoomRelDto.getRoomId());
        }


        RoomDto roomDto = new RoomDto();
        roomDto.setRoomIds(roomIds.toArray(new String[roomIds.size()]));
        roomDto.setCommunityId(communityId);
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
        resOwnerDto.setRooms(roomDtos);
        return resOwnerDto;
    }

    /**
     * 根据业主身份证查询
     *
     * @param communityId
     * @param searchValue
     * @return
     */
    private OwnerDto queryByOwnerIdCard(String communityId, String searchValue) {
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(communityId);
        ownerDto.setIdCard(searchValue);
        ownerDto.setOwnerTypeCd(OwnerDto.OWNER_TYPE_CD_OWNER);
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
        Assert.listOnlyOne(ownerDtos, "未找到业主信息或者查询到多条，请换其他条件查询");
        OwnerDto resOwnerDto = ownerDtos.get(0);
        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setOwnerId(ownerDtos.get(0).getOwnerId());
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);

        //没有房屋
        if (ownerRoomRelDtos == null || ownerRoomRelDtos.size() < 1) {
            return resOwnerDto;
        }
        List<String> roomIds = new ArrayList<>();
        for (OwnerRoomRelDto tmpOwnerRoomRelDto : ownerRoomRelDtos) {
            roomIds.add(tmpOwnerRoomRelDto.getRoomId());
        }


        RoomDto roomDto = new RoomDto();
        roomDto.setRoomIds(roomIds.toArray(new String[roomIds.size()]));
        roomDto.setCommunityId(communityId);
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
        resOwnerDto.setRooms(roomDtos);
        return resOwnerDto;
    }

    /**
     * 根据业主手机号查询
     *
     * @param communityId
     * @param searchValue
     * @return
     */
    private OwnerDto queryByOwnerTel(String communityId, String searchValue) {
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(communityId);
        ownerDto.setLink(searchValue);
        ownerDto.setOwnerTypeCd(OwnerDto.OWNER_TYPE_CD_OWNER);
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
        Assert.listOnlyOne(ownerDtos, "未找到业主信息或者查询到多条，请换其他条件查询");
        OwnerDto resOwnerDto = ownerDtos.get(0);
        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setOwnerId(ownerDtos.get(0).getOwnerId());
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);

        //没有房屋
        if (ownerRoomRelDtos == null || ownerRoomRelDtos.size() < 1) {
            return resOwnerDto;
        }
        List<String> roomIds = new ArrayList<>();
        for (OwnerRoomRelDto tmpOwnerRoomRelDto : ownerRoomRelDtos) {
            roomIds.add(tmpOwnerRoomRelDto.getRoomId());
        }


        RoomDto roomDto = new RoomDto();
        roomDto.setRoomIds(roomIds.toArray(new String[roomIds.size()]));
        roomDto.setCommunityId(communityId);
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
        resOwnerDto.setRooms(roomDtos);
        return resOwnerDto;
    }

    /**
     * 根据业主名称 查询
     *
     * @param communityId
     * @param searchValue
     * @return
     */
    private OwnerDto queryByOwnerName(String communityId, String searchValue) {

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(communityId);
        ownerDto.setName(searchValue);
        ownerDto.setOwnerTypeCd(OwnerDto.OWNER_TYPE_CD_OWNER);
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
        Assert.listOnlyOne(ownerDtos, "未找到业主信息或者查询到多条，请换其他条件查询");
        OwnerDto resOwnerDto = ownerDtos.get(0);
        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setOwnerId(ownerDtos.get(0).getOwnerId());
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);

        //没有房屋
        if (ownerRoomRelDtos == null || ownerRoomRelDtos.size() < 1) {
            return resOwnerDto;
        }
        List<String> roomIds = new ArrayList<>();
        for (OwnerRoomRelDto tmpOwnerRoomRelDto : ownerRoomRelDtos) {
            roomIds.add(tmpOwnerRoomRelDto.getRoomId());
        }


        RoomDto roomDto = new RoomDto();
        roomDto.setRoomIds(roomIds.toArray(new String[roomIds.size()]));
        roomDto.setCommunityId(communityId);
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
        resOwnerDto.setRooms(roomDtos);
        return resOwnerDto;
    }

    /**
     * 根据房屋查询
     *
     * @param communityId
     * @param searchValue
     * @return
     */
    private OwnerDto queryByRoom(String communityId, String searchValue) {

        if (!searchValue.contains("-")) {
            throw new IllegalArgumentException("查询内容格式错误，请输入 楼栋-单元-房屋 如 1-1-1");
        }

        String[] values = searchValue.split("-");

        if (values.length != 3) {
            throw new IllegalArgumentException("查询内容格式错误，请输入 楼栋-单元-房屋 如 1-1-1");
        }

        RoomDto roomDto = new RoomDto();
        roomDto.setFloorNum(values[0]);
        roomDto.setUnitNum(values[1]);
        roomDto.setRoomNum(values[2]);
        roomDto.setCommunityId(communityId);

        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

        Assert.listOnlyOne(roomDtos, "未找到房屋信息");

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(communityId);
        ownerDto.setRoomId(roomDtos.get(0).getRoomId());
        ownerDto.setOwnerTypeCd(OwnerDto.OWNER_TYPE_CD_OWNER);
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);

        Assert.listOnlyOne(ownerDtos, "未找到业主信息");

        OwnerDto resOwnerDto = ownerDtos.get(0);

        resOwnerDto.setRooms(roomDtos);

        return resOwnerDto;
    }


}