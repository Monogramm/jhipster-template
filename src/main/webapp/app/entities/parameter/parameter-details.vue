<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <div v-if="parameter">
                <h2 class="jh-entity-heading"><span v-text="$t('templateApp.parameter.detail.title')">Parameter</span> {{parameter.id}}</h2>
                <dl class="row jh-entity-details">
                    <dt>
                        <span v-text="$t('templateApp.parameter.name')">Name</span>
                    </dt>
                    <dd>
                        <span>{{parameter.name}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('templateApp.parameter.description')">Description</span>
                    </dt>
                    <dd>
                        <span>{{parameter.description}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('templateApp.parameter.type')">Type</span>
                    </dt>
                    <dd>
                        <span v-text="$t('templateApp.ParameterType.' + parameter.type)">{{parameter.type}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('templateApp.parameter.value')">Value</span>
                    </dt>
                    <dd>
                        <span>{{parameter.value}}</span>
                    </dd>
                </dl>
                <button type="submit"
                        v-on:click.prevent="previousState()"
                        class="btn btn-info">
                    <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
                </button>
                <router-link v-if="parameter.id" :to="{name: 'ParameterEdit', params: {parameterId: parameter.id}}" tag="button" class="btn btn-primary">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
                </router-link>
            </div>
        </div>
    </div>
</template>

<script lang="ts" src="./parameter-details.component.ts">
</script>
