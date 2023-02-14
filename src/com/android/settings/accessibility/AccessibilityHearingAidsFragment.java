/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.accessibility;

import static android.os.UserManager.DISALLOW_CONFIG_BLUETOOTH;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.preference.PreferenceCategory;

import com.android.internal.accessibility.AccessibilityShortcutController;
import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.search.SearchIndexable;

/** Accessibility settings for hearing aids. */
@SearchIndexable(forTarget = SearchIndexable.ALL & ~SearchIndexable.ARC)
public class AccessibilityHearingAidsFragment extends AccessibilityShortcutPreferenceFragment {

    private static final String TAG = "AccessibilityHearingAidsFragment";
    private static final String KEY_DEVICE_CONTROL_CATEGORY = "device_control_category";
    private static final int FIRST_PREFERENCE_IN_CATEGORY_INDEX = -1;
    private String mFeatureName;

    public AccessibilityHearingAidsFragment() {
        super(DISALLOW_CONFIG_BLUETOOTH);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        use(AvailableHearingDevicePreferenceController.class).init(this);
        use(SavedHearingDevicePreferenceController.class).init(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mFeatureName = getContext().getString(R.string.accessibility_hearingaid_title);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);
        final PreferenceCategory controlCategory = findPreference(KEY_DEVICE_CONTROL_CATEGORY);
        // Move the preference under controlCategory need to remove the original first.
        mShortcutPreference.setOrder(FIRST_PREFERENCE_IN_CATEGORY_INDEX);
        getPreferenceScreen().removePreference(mShortcutPreference);
        controlCategory.addPreference(mShortcutPreference);
        return view;
    }

    @Override
    public int getMetricsCategory() {
        // TODO(b/262839191): To be updated settings_enums.proto
        return 0;
    }

    @Override
    protected int getPreferenceScreenResId() {
        return R.xml.accessibility_hearing_aids;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected ComponentName getComponentName() {
        return AccessibilityShortcutController.ACCESSIBILITY_HEARING_AIDS_COMPONENT_NAME;
    }

    @Override
    protected CharSequence getLabelName() {
        return mFeatureName;
    }

    @Override
    protected ComponentName getTileComponentName() {
        // Don't have quick settings tile for now.
        return null;
    }

    @Override
    protected CharSequence getTileTooltipContent(int type) {
        // Don't have quick settings tile for now.
        return null;
    }

    @Override
    protected boolean showGeneralCategory() {
        // Have customized category for accessibility hearings aids page.
        return false;
    }

    @Override
    protected CharSequence getShortcutTitle() {
        return getText(R.string.accessibility_hearing_device_shortcut_title);
    }

    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider(R.xml.accessibility_hearing_aids);
}