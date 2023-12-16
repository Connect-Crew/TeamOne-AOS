package com.connectcrew.presentation.adapter.projectwrite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.ItemProjectWriteMemberRecruitmentBinding
import com.connectcrew.presentation.model.project.ProjectJobUiModel
import com.connectcrew.presentation.util.executeAfter
import com.connectcrew.presentation.util.listener.DebounceEditTextListener
import com.connectcrew.presentation.util.listener.setOnSingleClickListener
import kotlinx.coroutines.CoroutineScope

class ProjectMemberPartRecruitmentAdapter(
    private val coroutineScope: CoroutineScope,
    private val onRemoveProjectMemberPartRecruitment: (ProjectJobUiModel) -> Unit,
    private val onUpdateProjectMemberPartRecruitmentCount: (Int, ProjectJobUiModel) -> Unit,
    private val onUpdateProjectMemberPartRecruitmentComment: (Int, ProjectJobUiModel) -> Unit,
) : ListAdapter<ProjectJobUiModel, ProjectMemberPartRecruitmentViewHolder>(
    object : DiffUtil.ItemCallback<ProjectJobUiModel>() {
        override fun areItemsTheSame(oldItem: ProjectJobUiModel, newItem: ProjectJobUiModel): Boolean {
            return oldItem.key == newItem.key
        }

        override fun areContentsTheSame(oldItem: ProjectJobUiModel, newItem: ProjectJobUiModel): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: ProjectJobUiModel, newItem: ProjectJobUiModel): Any? {
            return if (oldItem.comment != newItem.comment || oldItem.maxCount != newItem.maxCount) {
                PAYLOAD_PROJECT_PART_RECRUITMENT
            } else {
                super.getChangePayload(oldItem, newItem)
            }
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectMemberPartRecruitmentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ProjectMemberPartRecruitmentViewHolder(ItemProjectWriteMemberRecruitmentBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ProjectMemberPartRecruitmentViewHolder, position: Int) {
        val jobUiModelItem = getItem(position) ?: return
        onBindViewHolder(holder.binding, position, jobUiModelItem)
    }

    override fun onBindViewHolder(holder: ProjectMemberPartRecruitmentViewHolder, position: Int, payloads: MutableList<Any>) {
        val jobUiModelItem = getItem(position) ?: return

        if (payloads.contains(PAYLOAD_PROJECT_PART_RECRUITMENT)) {
            onBindViewHolder(holder.binding, position, jobUiModelItem)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    private fun onBindViewHolder(binding: ItemProjectWriteMemberRecruitmentBinding, position: Int, jobUiModel: ProjectJobUiModel) {
        binding.executeAfter {
            projectJobUiModel = jobUiModel

            ivDelete.setOnSingleClickListener {
                onRemoveProjectMemberPartRecruitment(jobUiModel)
            }

            tvMinus.setOnClickListener {
                if (jobUiModel.maxCount > 1) {
                    onUpdateProjectMemberPartRecruitmentCount(position, jobUiModel.copy(maxCount = jobUiModel.maxCount.minus(1)))
                }
            }

            tvPlus.setOnClickListener {
                onUpdateProjectMemberPartRecruitmentCount(position, jobUiModel.copy(maxCount = jobUiModel.maxCount.plus(1)))
            }

            tietPartIntroduction.addTextChangedListener(DebounceEditTextListener(
                scope = coroutineScope,
                onDebounceEditTextChange = { onUpdateProjectMemberPartRecruitmentComment(position, jobUiModel.copy(comment = it)) }
            ))
        }
    }

    override fun getItemViewType(position: Int) = R.layout.item_project_write_member_recruitment

    companion object {
        private const val PAYLOAD_PROJECT_PART_RECRUITMENT = "payload_project_part_recruitment"
    }
}