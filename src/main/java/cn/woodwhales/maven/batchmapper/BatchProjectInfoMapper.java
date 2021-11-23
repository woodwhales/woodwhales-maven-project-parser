package cn.woodwhales.maven.batchmapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import cn.woodwhales.maven.entity.ProjectInfo;
import cn.woodwhales.maven.mapper.ProjectInfoMapper;

/**
 * BatchProjectInfoMapper
 *
 * @author woodwhales on 2021-11-16 11:31:40
 *
 */
@Service
public class BatchProjectInfoMapper extends ServiceImpl<ProjectInfoMapper, ProjectInfo> {
    
}