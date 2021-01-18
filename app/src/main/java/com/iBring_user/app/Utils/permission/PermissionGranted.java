package com.iBring_user.app.Utils.permission;

import java.util.ArrayList;

public interface PermissionGranted
{
    void showPermissionAlert(ArrayList<String> permissionList, int code);
}
