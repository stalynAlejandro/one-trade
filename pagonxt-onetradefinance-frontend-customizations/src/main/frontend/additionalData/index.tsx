import {_} from '@flowable/forms';
import {v1 as uuidv4} from 'uuid';

export const additionalData = {
    custom: {
        sampleReadableValue: 'foo',
        uuid(): String {
            return uuidv4();
        },
        setFieldToObjectArray(array: any[], field: string, value: any): object[] {
            if (array === undefined || array.length == 0) {
                return [];
            }

            return array.map((o) => {
                _.set(o, field, value);
                return o;
            });
        },
        triggerRefresh(): boolean {
            const element = document.querySelector(
                '.flw-customCaseDetails__sideBar__custom_item--refresh',
            );
            if (element instanceof HTMLElement) {
                element.click();
                return true;
            }
        },
    },
};
