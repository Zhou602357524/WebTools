package com.xwtec.tools.core;

import com.xwtec.tools.core.entity.PushParams;
import com.xwtec.tools.core.entity.UserInfoEntity;
import com.xwtec.tools.core.repository.PushRepository;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class ForkJoinTest extends RecursiveTask<Map<String, List<UserInfoEntity>>> {
    private final static ForkJoinPool POOL = new ForkJoinPool();
    PushParams pushParams;
    List<UserInfoEntity> list;
    ForkJoinTest(PushParams pushParams, List<UserInfoEntity> list) {
        this.pushParams = pushParams;
        this.list = list;
    }

    @Override
    protected Map<String, List<UserInfoEntity>> compute() {
        Map<String, List<UserInfoEntity>> resultMap = new HashMap<>(2);
        ForkJoinTask<List<UserInfoEntity>> androidTask = null;
        ForkJoinTask<List<UserInfoEntity>> IOSTask = null;
        try {
            if (pushParams.isVersion_android())
                androidTask = new PushParallelTask(pushParams,0,list.size(),VersionEnum.ANDROID).fork();

            if (pushParams.isVersion_ios())
                IOSTask = new PushParallelTask(pushParams,0,list.size(),VersionEnum.ANDROID).fork();
            list = null;
            if (androidTask != null) resultMap.put("android", androidTask.join());
            if (IOSTask != null) resultMap.put("IOS", IOSTask.join());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    private enum VersionEnum {
        ANDROID, IOS
    }

    private class PushParallelTask extends RecursiveTask<List<UserInfoEntity>> {
        private final VersionEnum version;
        private PushParams pushParams;
        private int begin;
        private int end;
        private final int MAX = 20000;
        private List<UserInfoEntity> list;

        private PushParallelTask(PushParams pushParams, int begin, int end, VersionEnum version) {
            this(pushParams, begin, end, new LinkedList<UserInfoEntity>(), version);
        }

        private PushParallelTask(PushParams pushParams, int begin, int end, List<UserInfoEntity> list, VersionEnum version) {
            this.pushParams = pushParams;
            this.begin = begin;
            this.end = end;
            this.list = list;
            this.version = version;
        }

        @Override
        protected List<UserInfoEntity> compute() {
            int total = end - begin;
            if (total < MAX)
                return process();
            int average = total / 2;
            PushParallelTask leftTask = new PushParallelTask(pushParams, begin, begin + average, list, version);
            leftTask.fork();
            PushParallelTask rightTask = new PushParallelTask(pushParams, begin + average + 1, end, list, version);
            rightTask.fork();
            list.addAll(rightTask.join());
            list.addAll(leftTask.join());
            return list;
        }

        private List<UserInfoEntity> process() {
            List<UserInfoEntity> list = null;
            if (version.equals(VersionEnum.ANDROID)) {
                if (pushParams.isShow_msgid() && pushParams.isShow_phone())
                    list = pushRepository.queryAndroidPhoneAndMsgid();
                else if (pushParams.isShow_phone())
                    list = pushRepository.queryAndroidPhone();
                else if (pushParams.isShow_msgid())
                    list = pushRepository.queryAndroidMsgid();
            } else if (version.equals(VersionEnum.IOS)){
                if (pushParams.isShow_msgid() && pushParams.isShow_phone())
                    list = pushRepository.queryIOSPhoneAndMsgid();
                else if (pushParams.isShow_phone())
                    list = pushRepository.queryIOSPhone();
                else if (pushParams.isShow_msgid())
                    list = pushRepository.queryIOSMsgid();
            }
            return list;
        }
    }

    PushRepository pushRepository;
}
