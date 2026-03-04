package com.loyalty.partner_service.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MergeSort {

    public static void mergeSort(int[] arr, int left, int right) {
        if (left >= right) {
            return; // Base case: single element
        }

        int mid = left + (right - left) / 2;

        // Divide
        mergeSort(arr, left, mid);
        mergeSort(arr, mid + 1, right);

        // Merge
        merge(arr, left, mid, right);
    }

    private static void merge(int[] arr, int left, int mid, int right) {

        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] leftArr = new int[n1];
        int[] rightArr = new int[n2];

        // Copy data
        for (int i = 0; i < n1; i++)
            leftArr[i] = arr[left + i];

        for (int j = 0; j < n2; j++)
            rightArr[j] = arr[mid + 1 + j];

        int i = 0, j = 0, k = left;

        // Merge two sorted arrays
        while (i < n1 && j < n2) {
            if (leftArr[i] <= rightArr[j]) {
                arr[k++] = leftArr[i++];
            } else {
                arr[k++] = rightArr[j++];
            }
        }

        // Copy remaining elements
        while (i < n1) {
            arr[k++] = leftArr[i++];
        }

        while (j < n2) {
            arr[k++] = rightArr[j++];
        }
    }

    private static int[] productExceptSelf(int[] nums) {

        int[] result = new int[nums.length];
        
        int pre = 1, post =1;
        
        for (int i = 0; i < nums.length; i++) {
            result[i] = pre;
            pre = nums[i] * pre;
        }

        for (int i= nums.length -1 ; i  >= 0; i--) {
            result[i] = post * result[i];
            post = nums[i] * post;
        }
        return result;
    }

    private ArrayList<List<String>> isAnagramOfList(String[] str) {

        HashMap<String, List<String>> anBlcks = new HashMap<>();

        for (String s : str) {

            char[] ch = s.toCharArray();
            Arrays.sort(ch);
            String key = new String(ch);
            if (!anBlcks.containsKey(key)) {
                anBlcks.put(key, new ArrayList<>());
            }
            anBlcks.get(key).add(s);
        }

        return new ArrayList<>(anBlcks.values());
    }

    public static void main(String[] args) {
        // int[] arr = { 38, 27, 43, 3, 9, 82, 10 };

        // mergeSort(arr, 0, arr.length - 1);

        // for (int num : arr) {
        //     System.out.print(num + " ");
        // }
        int [] nums = {1,2,3,4,5,6,7,8,9};
        int [] result = productExceptSelf(nums);

        log.info("result: {}", result);
    }
}
