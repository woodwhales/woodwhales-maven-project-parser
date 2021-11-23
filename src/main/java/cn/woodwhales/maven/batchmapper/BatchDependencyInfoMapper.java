package cn.woodwhales.maven.batchmapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import cn.woodwhales.maven.entity.DependencyInfo;
import cn.woodwhales.maven.mapper.DependencyInfoMapper;

/**
 * BatchDependencyInfoMapper
 *
 * @author woodwhales on 2021-11-16 11:31:40
 *
 */
@Service
public class BatchDependencyInfoMapper extends ServiceImpl<DependencyInfoMapper, DependencyInfo> {
    
}