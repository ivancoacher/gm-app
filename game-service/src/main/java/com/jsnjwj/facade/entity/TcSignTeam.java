package com.jsnjwj.facade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * @TableName tc_sign_team
 */
@TableName(value = "tc_sign_team")
public class TcSignTeam implements Serializable {

	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 *
	 */
	private Integer gameId;

	/**
	 *
	 */
	private String teamName;

	/**
	 *
	 */
	private String leaderName;

	/**
	 *
	 */
	private String leaderTel;

	/**
	 *
	 */
	private String coachName;

	/**
	 *
	 */
	private String coachTel;

	/**
	 *
	 */
	private String remark;

	/**
	 *
	 */
	private Date createTime;

	/**
	 *
	 */
	private Date updateTime;

	/**
	 *
	 */
	private Integer signStatus;

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	public Integer getId() {
		return id;
	}

	/**
	 *
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 *
	 */
	public Integer getGameId() {
		return gameId;
	}

	/**
	 *
	 */
	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}

	/**
	 *
	 */
	public String getTeamName() {
		return teamName;
	}

	/**
	 *
	 */
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	/**
	 *
	 */
	public String getLeaderName() {
		return leaderName;
	}

	/**
	 *
	 */
	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}

	/**
	 *
	 */
	public String getLeaderTel() {
		return leaderTel;
	}

	/**
	 *
	 */
	public void setLeaderTel(String leaderTel) {
		this.leaderTel = leaderTel;
	}

	/**
	 *
	 */
	public String getCoachName() {
		return coachName;
	}

	/**
	 *
	 */
	public void setCoachName(String coachName) {
		this.coachName = coachName;
	}

	/**
	 *
	 */
	public String getCoachTel() {
		return coachTel;
	}

	/**
	 *
	 */
	public void setCoachTel(String coachTel) {
		this.coachTel = coachTel;
	}

	/**
	 *
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 *
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 *
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 *
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 *
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 *
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 *
	 */
	public Integer getSignStatus() {
		return signStatus;
	}

	/**
	 *
	 */
	public void setSignStatus(Integer signStatus) {
		this.signStatus = signStatus;
	}

	@Override
	public boolean equals(Object that) {
		if (this == that) {
			return true;
		}
		if (that == null) {
			return false;
		}
		if (getClass() != that.getClass()) {
			return false;
		}
		TcSignTeam other = (TcSignTeam) that;
		return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
				&& (this.getGameId() == null ? other.getGameId() == null : this.getGameId().equals(other.getGameId()))
				&& (this.getTeamName() == null ? other.getTeamName() == null
						: this.getTeamName().equals(other.getTeamName()))
				&& (this.getLeaderName() == null ? other.getLeaderName() == null
						: this.getLeaderName().equals(other.getLeaderName()))
				&& (this.getLeaderTel() == null ? other.getLeaderTel() == null
						: this.getLeaderTel().equals(other.getLeaderTel()))
				&& (this.getCoachName() == null ? other.getCoachName() == null
						: this.getCoachName().equals(other.getCoachName()))
				&& (this.getCoachTel() == null ? other.getCoachTel() == null
						: this.getCoachTel().equals(other.getCoachTel()))
				&& (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
				&& (this.getCreateTime() == null ? other.getCreateTime() == null
						: this.getCreateTime().equals(other.getCreateTime()))
				&& (this.getUpdateTime() == null ? other.getUpdateTime() == null
						: this.getUpdateTime().equals(other.getUpdateTime()))
				&& (this.getSignStatus() == null ? other.getSignStatus() == null
						: this.getSignStatus().equals(other.getSignStatus()));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		result = prime * result + ((getGameId() == null) ? 0 : getGameId().hashCode());
		result = prime * result + ((getTeamName() == null) ? 0 : getTeamName().hashCode());
		result = prime * result + ((getLeaderName() == null) ? 0 : getLeaderName().hashCode());
		result = prime * result + ((getLeaderTel() == null) ? 0 : getLeaderTel().hashCode());
		result = prime * result + ((getCoachName() == null) ? 0 : getCoachName().hashCode());
		result = prime * result + ((getCoachTel() == null) ? 0 : getCoachTel().hashCode());
		result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
		result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
		result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
		result = prime * result + ((getSignStatus() == null) ? 0 : getSignStatus().hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
		sb.append(", id=").append(id);
		sb.append(", gameId=").append(gameId);
		sb.append(", teamName=").append(teamName);
		sb.append(", leaderName=").append(leaderName);
		sb.append(", leaderTel=").append(leaderTel);
		sb.append(", coachName=").append(coachName);
		sb.append(", coachTel=").append(coachTel);
		sb.append(", remark=").append(remark);
		sb.append(", createTime=").append(createTime);
		sb.append(", updateTime=").append(updateTime);
		sb.append(", signStatus=").append(signStatus);
		sb.append(", serialVersionUID=").append(serialVersionUID);
		sb.append("]");
		return sb.toString();
	}

}