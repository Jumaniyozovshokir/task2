package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Faculty;
import uz.pdp.appjparelationships.entity.Group;
import uz.pdp.appjparelationships.payload.GroupDto;
import uz.pdp.appjparelationships.repository.FacultyRepository;
import uz.pdp.appjparelationships.repository.GroupRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/group")
public class GroupController {

    final
    GroupRepository groupRepository;
    final
    FacultyRepository facultyRepository;

    public GroupController(GroupRepository groupRepository, FacultyRepository facultyRepository) {
        this.groupRepository = groupRepository;
        this.facultyRepository = facultyRepository;
    }

    //VAZIRLIK UCHUN
    //READ
    @GetMapping
    public List<Group> getGroups() {
        List<Group> groups = groupRepository.findAll();
        return groups;
    }


    //UNIVERSITET MAS'UL XODIMI UCHUN
    @GetMapping("/byUniversityId/{universityId}")
    public List<Group> getGroupsByUniversityId(@PathVariable Integer universityId) {
        List<Group> allByFaculty_universityId = groupRepository.findAllByFaculty_UniversityId(universityId);
        List<Group> groupsByUniversityId = groupRepository.getGroupsByUniversityId(universityId);
        List<Group> groupsByUniversityIdNative = groupRepository.getGroupsByUniversityIdNative(universityId);
        return allByFaculty_universityId;
    }

    @PostMapping
    public String addGroup(@RequestBody GroupDto groupDto) {

        Group group = new Group();
        group.setName(groupDto.getName());

        Optional<Faculty> optionalFaculty = facultyRepository.findById(groupDto.getFacultyId());
        if (!optionalFaculty.isPresent()) {
            return "Such faculty not found";
        }

        group.setFaculty(optionalFaculty.get());

        groupRepository.save(group);
        return "Group added";
    }

    @PutMapping("/{groupId}")
    public String updateGroup(@RequestBody GroupDto groupDto, @PathVariable Integer groupId){
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        if(optionalGroup.isPresent()){
            optionalGroup.get().setName(groupDto.getName());

            Optional<Faculty> optionalFaculty = facultyRepository.findById(groupDto.getFacultyId());
            if (!optionalFaculty.isPresent()) {
                return "Such faculty not found";
            }

            groupRepository.save(optionalGroup.get());
            return "Group updated!";
        }
        return "Group not found!";
    }

    @DeleteMapping("/{groupId}")
    public String deleteGroup(@PathVariable Integer groupId) {
        groupRepository.deleteById(groupId);
        return "Group deleted!";
    }

}
