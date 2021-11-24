package cn.woodwhales.maven.batchmapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import cn.woodwhales.maven.entity.ProjectInfo;
import cn.woodwhales.maven.mapper.ProjectInfoMapper;

/**
 * BatchProjectInfoMapper
 *
 * @author woodwhales on 2021-11-24 17:10:10
 *
 */
@Service
public class BatchProjectInfoMapper extends ServiceImpl<ProjectInfoMapper, ProjectInfo> {
    
}