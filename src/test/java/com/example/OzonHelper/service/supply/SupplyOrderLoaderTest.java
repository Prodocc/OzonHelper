package com.example.OzonHelper.service.supply;

import com.example.OzonHelper.client.OzonClient;
import com.example.OzonHelper.dto.response.fbo.SupplyOrderDto;
import com.example.OzonHelper.dto.response.fbo.SupplyOrdersPage;
import com.example.OzonHelper.enums.ozon.SupplyState;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class SupplyOrderLoaderTest {
    private final OzonClient client = mock(OzonClient.class);
    private final SupplyOrderLoader loader = new SupplyOrderLoader();

    @Test
    public void getAllSupplyOrderIds_OnePage_returnsAllOrderIds() throws IOException, InterruptedException {
        // 54
        SupplyOrdersPage page1 = new SupplyOrdersPage(
                IntStream.rangeClosed(1, 54).mapToObj(String::valueOf).toList(),
                "1");

        when(client.getSupplyOrdersIds(null, SupplyState.COMPLETED)).thenReturn(page1);

        List<String> expected = new ArrayList<>();
        expected.addAll(page1.orderIds());

        List<String> actual = loader.getAllSupplyOrderIds(client, SupplyState.COMPLETED);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void getAllSupplyOrderIds_twoPages_returnsAllOrderIds() throws IOException, InterruptedException {
        // 100, 17
        SupplyOrdersPage page1 = new SupplyOrdersPage(
                IntStream.rangeClosed(1, 100).mapToObj(String::valueOf).toList(),
                "1");
        SupplyOrdersPage page2 = new SupplyOrdersPage(
                IntStream.rangeClosed(101, 117).mapToObj(String::valueOf).toList(),
                "");

        when(client.getSupplyOrdersIds(null, SupplyState.COMPLETED)).thenReturn(page1);
        when(client.getSupplyOrdersIds("1", SupplyState.COMPLETED)).thenReturn(page2);

        List<String> expected = new ArrayList<>();
        expected.addAll(page1.orderIds());
        expected.addAll(page2.orderIds());

        List<String> actual = loader.getAllSupplyOrderIds(client, SupplyState.COMPLETED);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void getAllSupplyOrderIds_threePages_returnsAllOrderIds() throws IOException, InterruptedException {
        // 100, 100, 17
        SupplyOrdersPage page1 = new SupplyOrdersPage(
                IntStream.rangeClosed(1, 100).mapToObj(String::valueOf).toList(),
                "1");
        SupplyOrdersPage page2 = new SupplyOrdersPage(
                IntStream.rangeClosed(101, 200).mapToObj(String::valueOf).toList(),
                "2");
        SupplyOrdersPage page3 = new SupplyOrdersPage(
                IntStream.rangeClosed(201, 217).mapToObj(String::valueOf).toList(),
                "");

        when(client.getSupplyOrdersIds(null, SupplyState.COMPLETED)).thenReturn(page1);
        when(client.getSupplyOrdersIds("1", SupplyState.COMPLETED)).thenReturn(page2);
        when(client.getSupplyOrdersIds("2", SupplyState.COMPLETED)).thenReturn(page3);

        List<String> expected = new ArrayList<>();
        expected.addAll(page1.orderIds());
        expected.addAll(page2.orderIds());
        expected.addAll(page3.orderIds());

        List<String> actual = loader.getAllSupplyOrderIds(client, SupplyState.COMPLETED);

        assertThat(actual).isEqualTo(expected);

        verify(client, times(3)).getSupplyOrdersIds(any(), any(SupplyState.class));
    }

    @Test
    public void getSupplyOrders_NoIds_shouldNotCallApi() throws IOException, InterruptedException {
        List<String> supplyOrderIds = List.of();
        List<SupplyOrderDto> supplyOrderDtos = List.of();

        List<SupplyOrderDto> expected = new ArrayList<>(supplyOrderDtos);

        List<SupplyOrderDto> actual = loader.getSupplyOrderDtos(client, supplyOrderIds);

        assertThat(actual).isEqualTo(expected);

        verify(client, never()).getSupplyOrders(any());
    }

    @Test
    public void getSupplyOrders_45Ids_shouldCallApiOneTime() throws IOException, InterruptedException {
        List<String> supplyOrderIds = IntStream.rangeClosed(1, 45).mapToObj(String::valueOf).toList();
        List<SupplyOrderDto> supplyOrderDtos = new ArrayList<>();
        for (int i = 0; i < supplyOrderIds.size(); i++) {
            supplyOrderDtos.add(new SupplyOrderDto());
        }

        when(client.getSupplyOrders(supplyOrderIds)).thenReturn(supplyOrderDtos);

        List<SupplyOrderDto> expected = new ArrayList<>(supplyOrderDtos);

        List<SupplyOrderDto> actual = loader.getSupplyOrderDtos(client, supplyOrderIds);

        assertThat(actual).isEqualTo(expected);

        verify(client).getSupplyOrders(supplyOrderIds);
    }

    @Test
    public void getSupplyOrders_50Ids_shouldCallApiOneTime() throws IOException, InterruptedException {
        List<String> supplyOrderIds = IntStream.rangeClosed(1, 50).mapToObj(String::valueOf).toList();
        List<SupplyOrderDto> supplyOrderDtos = new ArrayList<>();
        for (int i = 0; i < supplyOrderIds.size(); i++) {
            supplyOrderDtos.add(new SupplyOrderDto());
        }

        when(client.getSupplyOrders(supplyOrderIds)).thenReturn(supplyOrderDtos);

        List<SupplyOrderDto> expected = new ArrayList<>(supplyOrderDtos);

        List<SupplyOrderDto> actual = loader.getSupplyOrderDtos(client, supplyOrderIds);

        assertThat(actual).isEqualTo(expected);

        verify(client).getSupplyOrders(supplyOrderIds);
    }

    @Test
    public void getSupplyOrders_51Ids_shouldCallApiTwoTimes() throws IOException, InterruptedException {
        List<String> supplyOrderIds = IntStream.rangeClosed(1, 51).mapToObj(String::valueOf).toList();
        List<SupplyOrderDto> supplyOrderDtos = new ArrayList<>();
        for (int i = 0; i < supplyOrderIds.size(); i++) {
            supplyOrderDtos.add(new SupplyOrderDto());
        }

        when(client.getSupplyOrders(supplyOrderIds.subList(0, 50))).thenReturn(supplyOrderDtos.subList(0, 50));
        when(client.getSupplyOrders(supplyOrderIds.subList(50, 51))).thenReturn(supplyOrderDtos.subList(50, 51));

        List<SupplyOrderDto> expected = new ArrayList<>(supplyOrderDtos);

        List<SupplyOrderDto> actual = loader.getSupplyOrderDtos(client, supplyOrderIds);

        assertThat(actual).isEqualTo(expected);

        verify(client).getSupplyOrders(supplyOrderIds.subList(0, 50));
        verify(client).getSupplyOrders(supplyOrderIds.subList(50, 51));
    }

    @Test
    public void getSupplyOrders_101Ids_shouldCallApiThreeTimes() throws IOException, InterruptedException {
        List<String> supplyOrderIds = IntStream.rangeClosed(1, 101).mapToObj(String::valueOf).toList();
        List<SupplyOrderDto> supplyOrderDtos = new ArrayList<>();
        for (int i = 0; i < supplyOrderIds.size(); i++) {
            supplyOrderDtos.add(new SupplyOrderDto());
        }

        when(client.getSupplyOrders(supplyOrderIds.subList(0, 50))).thenReturn(supplyOrderDtos.subList(0, 50));
        when(client.getSupplyOrders(supplyOrderIds.subList(50, 100))).thenReturn(supplyOrderDtos.subList(50, 100));
        when(client.getSupplyOrders(supplyOrderIds.subList(100, 101))).thenReturn(supplyOrderDtos.subList(100, 101));

        List<SupplyOrderDto> expected = new ArrayList<>(supplyOrderDtos);

        List<SupplyOrderDto> actual = loader.getSupplyOrderDtos(client, supplyOrderIds);

        assertThat(actual).isEqualTo(expected);

        verify(client).getSupplyOrders(supplyOrderIds.subList(0, 50));
        verify(client).getSupplyOrders(supplyOrderIds.subList(50, 100));
        verify(client).getSupplyOrders(supplyOrderIds.subList(100, 101));
    }
}
