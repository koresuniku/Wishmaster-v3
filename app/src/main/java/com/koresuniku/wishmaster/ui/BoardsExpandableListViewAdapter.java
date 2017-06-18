package com.koresuniku.wishmaster.ui;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koresuniku.wishmaster.R;
import com.koresuniku.wishmaster.http.boards_api.model.BoardsJsonSchema;
import com.koresuniku.wishmaster.ui.view.ExpandableListViewView;

import java.util.List;

public class BoardsExpandableListViewAdapter extends BaseExpandableListAdapter {

    private ExpandableListViewView mView;
    private BoardsJsonSchema mSchema;
    private Activity mActivity;

    public BoardsExpandableListViewAdapter(ExpandableListViewView view) {
        this.mView = view;
        mSchema = view.getSchema();
        mActivity = view.getActivity();
    }

    @Override
    public int getGroupCount() {
        return 9;
    }

    private List<?> getBoardsGroup(int i) {
        switch (i) {
            case 0: {
                return mSchema.getAdults();
            }
            case 1: {
                return mSchema.getGames();
            }
            case 2: {
                return mSchema.getPolitics();
            }
            case 3: {
                return mSchema.getUsers();
            }
            case 4: {
                return mSchema.getOther();
            }
            case 5: {
                return mSchema.getCreativity();
            }
            case 6: {
                return mSchema.getSubject();
            }
            case 7: {
                return mSchema.getTech();
            }
            case 8: {
                return mSchema.getJapanese();
            }
            default: return null;
        }
    }

    private String getBoardsGroupName(int i) {
        switch (i) {
            case 0: {
                return mActivity.getString(R.string.adult_boards);
            }
            case 1: {
                return mActivity.getString(R.string.games_boards);
            }
            case 2: {
                return mActivity.getString(R.string.politics_boards);
            }
            case 3: {
                return mActivity.getString(R.string.users_boards);
            }
            case 4: {
                return mActivity.getString(R.string.different_boards);
            }
            case 5: {
                return mActivity.getString(R.string.creativity_boards);
            }
            case 6: {
                return mActivity.getString(R.string.subject_boards);
            }
            case 7: {
                return mActivity.getString(R.string.tech_boards);
            }
            case 8: {
                return mActivity.getString(R.string.japanese_boards);
            }
            default: return null;
        }
    }

    @Override
    public int getChildrenCount(int i) {
        return getBoardsGroup(i).size();

    }

    @Override
    public Object getGroup(int i) {
        return getBoardsGroup(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return getBoardsGroup(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        view = mActivity.getLayoutInflater()
                .inflate(R.layout.boards_exp_listview_group_item, viewGroup, false);

        TextView groupName = (TextView) view.findViewById(R.id.group_board_name);
        groupName.setText(getBoardsGroupName(i));

        ImageView indicator = (ImageView) view.findViewById(R.id.group_item_indicator);
        if (b) indicator.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
        else indicator.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);

        view.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.exp_listview_child_selector));

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        view = mActivity.getLayoutInflater()
                .inflate(R.layout.boards_exp_listview_child_item, viewGroup, false);

        TextView boardNameTextView = (TextView) view.findViewById(R.id.child_board_name);
        final String boardId;
        final String boardName;
        switch (i) {
            case 0: {
                boardId = mSchema.getAdults().get(i1).getId();
                boardName = mSchema.getAdults().get(i1).getName();
                break;
            }
            case 1: {
                boardId = mSchema.getGames().get(i1).getId();
                boardName = mSchema.getGames().get(i1).getName();
                break;
            }
            case 2: {
                boardId = mSchema.getPolitics().get(i1).getId();
                boardName = mSchema.getPolitics().get(i1).getName();
                break;
            }
            case 3: {
                boardId = mSchema.getUsers().get(i1).getId();
                boardName = mSchema.getUsers().get(i1).getName();
                break;
            }
            case 4: {
                boardId = mSchema.getOther().get(i1).getId();
                boardName = mSchema.getOther().get(i1).getName();
                break;
            }
            case 5: {
                boardId = mSchema.getCreativity().get(i1).getId();
                boardName = mSchema.getCreativity().get(i1).getName();
                break;
            }
            case 6: {
                boardId = mSchema.getSubject().get(i1).getId();
                boardName = mSchema.getSubject().get(i1).getName();
                break;
            }
            case 7: {
                boardId = mSchema.getTech().get(i1).getId();
                boardName = mSchema.getTech().get(i1).getName();
                break;
            }
            case 8: {
                boardId = mSchema.getJapanese().get(i1).getId();
                boardName = mSchema.getJapanese().get(i1).getName();
                break;
            }
            default: {
                boardId = null;
                boardName = null;
            }
        }
        boardNameTextView.setText("/" + boardId + "/ - " + boardName);

        view.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.exp_listview_child_selector));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mView.onBoardsSelected(boardId, boardName);
            }
        });

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}