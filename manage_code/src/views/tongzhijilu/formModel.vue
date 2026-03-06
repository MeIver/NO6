<template>
	<el-dialog :title="formTitle" v-model="dialogVisible" width="600px">
		<el-form :model="form" label-width="120px" :disabled="formType === 'info'">
			<el-form-item label="就诊通知ID">
				<el-input v-model="form.jiuzhentongzhiId" placeholder="就诊通知ID" disabled></el-input>
			</el-form-item>
			<el-form-item label="关联预约ID">
				<el-input v-model="form.yuyueId" placeholder="关联预约ID" disabled></el-input>
			</el-form-item>
			<el-form-item label="用户账号">
				<el-input v-model="form.zhanghao" placeholder="用户账号" disabled></el-input>
			</el-form-item>
			<el-form-item label="手机号">
				<el-input v-model="form.shouji" placeholder="手机号" disabled></el-input>
			</el-form-item>
			<el-form-item label="通知类型">
				<el-input v-model="form.tongzhiLeixing" placeholder="通知类型" disabled></el-input>
			</el-form-item>
			<el-form-item label="发送时间">
				<el-input v-model="form.fasonShijian" placeholder="发送时间" disabled></el-input>
			</el-form-item>
			<el-form-item label="发送状态">
				<el-tag :type="form.fasonZhuangtai === '成功' ? 'success' : 'danger'">
					{{form.fasonZhuangtai}}
				</el-tag>
			</el-form-item>
			<el-form-item label="失败原因" v-if="form.fasonZhuangtai === '失败'">
				<el-input type="textarea" v-model="form.shibaiYuanyin" rows="3" disabled></el-input>
			</el-form-item>
			<el-form-item label="重试次数">
				<el-input v-model="form.chongshiCishu" placeholder="重试次数" disabled></el-input>
			</el-form-item>
			<el-form-item label="下次重试时间" v-if="form.xiaciChongshiShijian">
				<el-input v-model="form.xiaciChongshiShijian" placeholder="下次重试时间" disabled></el-input>
			</el-form-item>
			<el-form-item label="处理状态">
				<el-tag :type="getChuliZhuangtaiType(form.chuliZhuangtai)">
					{{form.chuliZhuangtai}}
				</el-tag>
			</el-form-item>
			<el-form-item label="处理备注" v-if="form.chuliBeizhu">
				<el-input type="textarea" v-model="form.chuliBeizhu" rows="3" disabled></el-input>
			</el-form-item>
			<el-form-item label="处理时间" v-if="form.chuliShijian">
				<el-input v-model="form.chuliShijian" placeholder="处理时间" disabled></el-input>
			</el-form-item>
		</el-form>
		<template #footer>
			<el-button @click="dialogVisible = false">关闭</el-button>
		</template>
	</el-dialog>
</template>

<script setup>
import { ref, reactive, getCurrentInstance } from 'vue'

const context = getCurrentInstance()?.appContext.config.globalProperties;
const emit = defineEmits(['formModelChange'])

const dialogVisible = ref(false)
const formType = ref('info')
const formTitle = ref('通知记录详情')

const form = ref({
	id: null,
	jiuzhentongzhiId: null,
	yuyueId: null,
	zhanghao: '',
	shouji: '',
	tongzhiLeixing: '',
	fasonShijian: '',
	fasonZhuangtai: '',
	shibaiYuanyin: '',
	chongshiCishu: 0,
	xiaciChongshiShijian: '',
	chuliZhuangtai: '',
	chuliBeizhu: '',
	chuliShijian: ''
})

const getChuliZhuangtaiType = (status) => {
	switch(status) {
		case '已处理': return 'success'
		case '未处理': return 'warning'
		case '已忽略': return 'info'
		default: return ''
	}
}

const init = (id = null, type = 'info') => {
	formType.value = type
	formTitle.value = type === 'info' ? '通知记录详情' : '编辑通知记录'
	
	if (id) {
		context.$http({
			url: `tongzhijilu/info/${id}`,
			method: 'get'
		}).then(res => {
			form.value = res.data.data
			dialogVisible.value = true
		})
	} else {
		dialogVisible.value = true
	}
}

defineExpose({
	init
})
</script>
