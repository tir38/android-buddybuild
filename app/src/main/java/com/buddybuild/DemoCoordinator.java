package com.buddybuild;

import com.buddybuild.core.App;
import com.buddybuild.core.Branch;
import com.buddybuild.core.Build;
import com.buddybuild.core.LogItem;
import com.buddybuild.rest.LoginResult;

import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * A {@link Coordinator} that supplies dummy models for demoing the app
 */
public class DemoCoordinator implements Coordinator {

    private DemoManager demoManager;

    private List<App> apps;
    private Map<String, List<Branch>> appsBranchesMap;
    private List<LogItem> logs;
    private List<Build> builds;

    public DemoCoordinator(DemoManager demoManager) {
        this.demoManager = demoManager;
    }

    @Override
    public Single<LoginResult> login(String email, String password) {
        setupDemoData();
        return Single.just(LoginResult.getDemoLoginResult());
    }

    @Override
    public Single<Boolean> logout() {
        demoManager.setDemoMode(false);
        clearDemoData();
        return Single.just(true);
    }

    @Override
    public boolean isLoggedIn() {
        return true;
    }

    @Override
    public String getLoggedInUserEmail() {
        return "Demo Mode";
    }

    @Override
    public Single<List<App>> getApps() {
        return Single.just(apps);
    }

    @Override
    public Single<List<Branch>> getBranches(String appId) {
        return Single.just(appsBranchesMap.get(appId));
    }

    @Override
    public Maybe<Build> getBuild(String buildId) {
        for (Build build : builds) {
            if (build.getId().equals(buildId)) {
                return Maybe.just(build);
            }
        }
        return Maybe.empty();
    }

    @Override
    public Single<List<LogItem>> getLogs(String buildId) {
        getBuild(buildId);
        return Single.just(logs);
    }

    private void setupDemoData() {
        apps = new ArrayList<>();
        appsBranchesMap = new HashMap<>();
        setupDemoBuilds();
        setupDemoAndroidApp();
        setupDemoIosApp();
        setupDemoLogs();
    }

    private void setupDemoIosApp() {
        App iosApp = new App("2", "2048 iOS app", App.Platform.IOS);
        apps.add(iosApp);

        List<Branch> branches = new ArrayList<>();
        Branch masterBranch = new Branch("master");
        masterBranch.setBuilds(builds);
        branches.add(masterBranch);

        Branch featureBranch = new Branch("feature/multiplayer");
        featureBranch.setBuilds(builds);
        branches.add(featureBranch);

        appsBranchesMap.put(iosApp.getId(), branches);
    }

    private void setupDemoAndroidApp() {
        App androidApp = new App("1", "2048 android app", App.Platform.ANDROID);
        apps.add(androidApp);

        List<Branch> branches = new ArrayList<>();
        Branch masterBranch = new Branch("master");
        masterBranch.setBuilds(builds);
        branches.add(masterBranch);

        Branch featureBranch = new Branch("feature/multiplayer");
        featureBranch.setBuilds(builds);
        branches.add(featureBranch);

        appsBranchesMap.put(androidApp.getId(), branches);
    }

    private void setupDemoBuilds() {
        builds = new ArrayList<>();

        builds.add(new Build.Builder()
                .id("1")
                .buildNumber(1)
                .author("Ali Connors")
                .buildStatus(Build.Status.SUCCESS)
                .commitMessage("Update game controller to allow multiplayer")
                .createTime(ZonedDateTime.now().minus(65, ChronoUnit.MINUTES))
                .startTime(ZonedDateTime.now().minus(60, ChronoUnit.MINUTES))
                .finishTime(ZonedDateTime.now().minus(50, ChronoUnit.MINUTES))
                .build());

        builds.add(new Build.Builder()
                .id("2")
                .buildNumber(2)
                .author("Aaron Bennett")
                .buildStatus(Build.Status.FAILED)
                .commitMessage("Derezz the master control program")
                .createTime(ZonedDateTime.now().minus(70, ChronoUnit.MINUTES))
                .startTime(ZonedDateTime.now().minus(68, ChronoUnit.MINUTES))
                .finishTime(ZonedDateTime.now().minus(60, ChronoUnit.MINUTES))
                .build());
    }

    private void setupDemoLogs() {
        logs = new ArrayList<>();
        logs.add(new LogItem(ZonedDateTime.now().minus(400, ChronoUnit.SECONDS),
                "logs", LogItem.Level.CT));
        logs.add(new LogItem(ZonedDateTime.now().minus(399, ChronoUnit.SECONDS),
                "    and logs", LogItem.Level.CI));
        logs.add(new LogItem(ZonedDateTime.now().minus(398, ChronoUnit.SECONDS),
                "    and logs", LogItem.Level.CI));
        logs.add(new LogItem(ZonedDateTime.now().minus(397, ChronoUnit.SECONDS),
                "    and logs", LogItem.Level.CW));
        logs.add(new LogItem(ZonedDateTime.now().minus(396, ChronoUnit.SECONDS),
                "        and logs", LogItem.Level.CE));
        logs.addAll(logs);
        logs.addAll(logs);
        logs.addAll(logs);
        logs.addAll(logs);
        logs.addAll(logs);
        logs.addAll(logs);
    }

    private void clearDemoData() {
        apps = null;
        appsBranchesMap = null;
        logs = null;
        builds = null;
    }
}
